package com.kira.farm_fresh_store.service.order;

import com.kira.farm_fresh_store.dto.order.OrderDetailDto;
import com.kira.farm_fresh_store.dto.order.OrderDto;
import com.kira.farm_fresh_store.entity.order.Order;
import com.kira.farm_fresh_store.entity.order.OrderDetail;
import com.kira.farm_fresh_store.entity.product.Product;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.exception.UserNotAuthenticatedException;
import com.kira.farm_fresh_store.repository.OrderRepository;
import com.kira.farm_fresh_store.repository.ProductRepository;
import com.kira.farm_fresh_store.repository.UserRepository;
import com.kira.farm_fresh_store.request.order.CreateOrderRequest;
import com.kira.farm_fresh_store.request.order.OrderDetailRequest;
import com.kira.farm_fresh_store.utils.AuthenUtil;
import com.kira.farm_fresh_store.utils.Util;
import com.kira.farm_fresh_store.utils.enums.Status;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final RuntimeService runtimeService;

    private final OrderRepository orderRepository;

    private final ModelMapper modelMapper;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final TaskService taskService;

    private final Util util;

    @Override
    public OrderDto createOrder(CreateOrderRequest request) {
        // 1. Khởi chạy quy trình Camunda
        String businessKey = util.generateRandomID();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "Process_1uxj2sj", businessKey);


        // 2. Lấy task "Kiểm tra đăng nhập"
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();

        // 3. Kiểm tra đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && !(authentication instanceof AnonymousAuthenticationToken);

        Map<String, Object> variables = new HashMap<>();
        variables.put("auth", isAuthenticated);
        variables.put("businessKey", businessKey);
        variables.put("productIds", request.getOrder_details().stream()
                .map(OrderDetailRequest::getProduct_id)
                .collect(Collectors.toList()));

        for (OrderDetailRequest detail : request.getOrder_details()) {
            variables.put("quantityOrder_" + detail.getProduct_id(), detail.getQuantityOrder());
        }

        // Nếu có task thì hoàn thành nó với biến auth
        if (task != null) {
            taskService.complete(task.getId(), variables);
        }

        // 4. Nếu không đăng nhập, ném lỗi
        if (!isAuthenticated) {
            throw new UserNotAuthenticatedException("Bạn vẫn chưa đăng nhập");
        }

        // 5. Lấy user ID từ token
        long userId = AuthenUtil.getProfileId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotAuthenticatedException("Không tìm thấy ID người dùng"));

        // 6. Tạo Order mới
        Order order = new Order();
        Order lastOrder = orderRepository.findFirstByOrderByIdDesc();
        if (lastOrder == null) {
            order.setId(util.createNewID("OD"));
        } else {
            order.setId(util.createIDFromLastID("OD", 2, lastOrder.getId()));
        }
        order.setUser(user);
        order.setBussinessKey(businessKey);
        order.setStatus(Status.PENDING);
        order.setOrderInfo(request.getOrderInfo());

        double totalPrice = 0;
        int totalItem = 0;
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (OrderDetailRequest orderDetailDto : request.getOrder_details()) {
            Product product = productRepository.findById(orderDetailDto.getProduct_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantityOrder(orderDetailDto.getQuantityOrder());
            orderDetail.setPrice(product.getPrice() * orderDetailDto.getQuantityOrder());
            orderDetail.setColor(orderDetailDto.getColor());

            totalPrice += orderDetail.getPrice();
            totalItem += orderDetail.getQuantityOrder();

            orderDetails.add(orderDetail);
        }

        order.setOrderDetails(orderDetails);
        order.setTotalPrice(totalPrice);
        order.setTotalItem(totalItem);

        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder, OrderDto.class);
    }

}
