package com.kira.farm_fresh_store.controller;

import com.kira.farm_fresh_store.config.VnpayConfig;
import com.kira.farm_fresh_store.entity.order.Order;
import com.kira.farm_fresh_store.entity.order.Transaction;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.OrderRepository;
import com.kira.farm_fresh_store.repository.TransactionRepository;
import com.kira.farm_fresh_store.repository.UserRepository;
import com.kira.farm_fresh_store.request.order.CreateVNPAYRequest;
import com.kira.farm_fresh_store.service.VNPay.IVnpayService;
import com.kira.farm_fresh_store.utils.AuthenUtil;
import com.kira.farm_fresh_store.utils.enums.Status;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VNPayController {

    private final IVnpayService vnPayService;

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    private final OrderRepository orderRepository;

    private final TaskService taskService;


    @RequestMapping(value = "/api/v1/vnpay/submitOrder/{businessKey}", produces = "application/json;charset=UTF-8")
    public String submitOrder(@PathVariable String businessKey,
                              HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return vnPayService.createOrder(businessKey, baseUrl, request.getRemoteAddr());
    }

    @RequestMapping("/api/v1/vnpay/getPaymentInfo")
    public boolean getPaymentInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
            int paymentStatus = vnPayService.orderReturn(request);

        Transaction transaction = new Transaction();

        // Lấy và kiểm tra giá trị từ request
        String transactionNo = request.getParameter("vnp_TransactionNo");
        String billNo = request.getParameter("vnp_TxnRef");
        String bankCode = request.getParameter("vnp_BankCode");
        String cardType = request.getParameter("vnp_CardType");
        String amountStr = request.getParameter("vnp_Amount");

        // Kiểm tra giá trị null trước khi parse
        long amount = (amountStr != null && !amountStr.isEmpty()) ? Integer.parseInt(amountStr) : 0;

        String orderInfo = request.getParameter("vnp_OrderInfo");
        Long userID = null;
        String orderId = null;
        String reason = "";

        if (orderInfo != null && orderInfo.contains("-") && orderInfo.contains("|")) {
            String[] arrayInfo = orderInfo.split("-"); // Tách userID và orderId bằng dấu "-"
            if (arrayInfo.length >= 2) {
                userID = Long.parseLong(arrayInfo[0]); // Lấy userID
                String[] orderInfoParts = arrayInfo[1].split("\\|"); // Tách orderId và mô tả thanh toán
                if (orderInfoParts.length >= 1) {
                    orderId = orderInfoParts[0]; // Lấy orderId
                }
                if (orderInfoParts.length >= 2) {
                    reason = orderInfoParts[1]; // Lấy phần mô tả thanh toán
                }
            }
        }

        // Set giá trị vào transaction
        transaction.setBillNo(billNo);
        transaction.setTransNo(UUID.randomUUID().toString() );
        transaction.setBankCode(bankCode);
        transaction.setCardType(cardType);
        transaction.setAmount(amount);
        transaction.setCurrency("VND");
        transaction.setCreateDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        transaction.setUser(userID != null
                ? userRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"))
                : null);
        transaction.setReason(reason);
        // Kiểm tra nếu orderId không null, tìm order từ DB
        if (orderId == null) {
            return false;
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
        Map<String, Object> variables = new HashMap<>();
        Task task = taskService.createTaskQuery()
                .processVariableValueEquals("businessKey", order.getBusinessKey()) // Truy vấn theo process variable
                .singleResult(); // Lấy task đầu tiên
        if (task == null) {
            log.error("Không tìm thấy task cho businessKey: " + order.getBusinessKey());
            return false;
        }
        transaction.setTblOrder(order);  // Gán Order vào Transaction
        // Xử lý trạng thái thanh toán
        if (paymentStatus == 1) {
            transaction.setStatus(Status.SUCCESS);
            transactionRepository.save(transaction);
            variables.put("payment", true);
            taskService.complete(task.getId(), variables);
            response.sendRedirect("http://localhost:4200/success/");
            return true;
        } else{
            transaction.setStatus(Status.FAIL);
            transactionRepository.save(transaction);
            variables.put("payment", false);
            taskService.complete(task.getId(), variables);
            response.sendRedirect("http://localhost:4200/failed" );
            return false;
        }
    }
}
