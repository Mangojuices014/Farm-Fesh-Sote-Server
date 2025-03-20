package com.kira.farm_fresh_store.service.VNPay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kira.farm_fresh_store.config.VnpayConfig;
import com.kira.farm_fresh_store.entity.order.Order;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.OrderRepository;
import com.kira.farm_fresh_store.repository.TransactionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VnpayService implements IVnpayService{

    private static final Logger log = LoggerFactory.getLogger(VnpayService.class);
    private final TransactionRepository transactionRepository;

    private final OrderRepository orderRepository;

    private final TaskService taskService;

    public String createOrder(String businessKey, String urlReturn, String ipAddr) {
        Order order = orderRepository.findByBusinessKey(businessKey)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
        if(transactionRepository.findByTransNo(vnp_TxnRef) != null){
            vnp_TxnRef = VnpayConfig.getRandomNumber(8);
        }

        String vnp_TmnCode = VnpayConfig.vnp_TmnCode;
        String orderType = "order-type";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(order.getTotalPrice() * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        String orderInfo = order.getId() + "|Thanh toán đơn hàng";  // Dùng ký tự phân tách an toàn hơn
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);
        urlReturn = VnpayConfig.vnp_ReturnURL; // Chỉ dùng giá trị từ config
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        vnp_Params.put("vnp_IpAddr", ipAddr);

        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(currentTime);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        String vnp_ExpireDate = formatter.format(currentTime.plusMinutes(15));
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                hashData.append(fieldName).append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                            .append('=')
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl.trim();
        paymentUrl = paymentUrl.replace(" ", "%20"); // Xóa dấu cách, nếu có

        // Tạo đối tượng JSON trả về
        Map<String, String> responseJson = new HashMap<>();
        responseJson.put("vnp_Returnurl", paymentUrl);

        // Chuyển đổi Map thành JSON string
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.writeValueAsString(responseJson);
        } catch (Exception e) {
            return "{\"error\":\"Unable to generate JSON\"}";
        }
    }

    @Override
    public int orderReturn(HttpServletRequest request) {
        Map<String, Object> variables = new HashMap<>();
        Map fields = new HashMap();
        for(Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = null;
            String fieldValue = null;
            try {
                fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            } catch(UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if(fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if(fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VnpayConfig.hashAllFields(fields);
        if(signValue.equals(vnp_SecureHash)) {
            if("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                variables.put("requiresManualConfirmation", 1);
                return 1;
            } else {
                variables.put("requiresManualConfirmation", 0);
                return 0;
            }
        } else {
            return -1;
        }
    }
}
