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
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final RuntimeService runtimeService;

    private final OrderRepository orderRepository;

    private final ModelMapper modelMapper;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final Util util;

    @Override
    public OrderDto createOrder(CreateOrderRequest request) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1uxj2sj", util.generateRandomID());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new ResourceNotFoundException("Bạn vẫn chưa đăng nhập");
        }
        long userId = AuthenUtil.getProfileId();
        // Kiểm tra và lấy User từ user_id
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotAuthenticatedException("Không tìm thấy ID người dùng"));

        // Tạo Order mới
        Order order = new Order();
        Order lastOrder = orderRepository.findFirstByOrderByIdDesc();
        if (lastOrder == null) {
            order.setId(util.createNewID("OD"));
        } else {
            order.setId(util.createIDFromLastID("OD", 2, lastOrder.getId()));
        }
        order.setUser(user);
        order.setBussinessKey(processInstance.getBusinessKey());
        order.setStatus(Status.NEW);
        order.setOrderInfo(request.getOrderInfo());
        double totalPrice = 0;
        int totalItem = 0;
        List<OrderDetail> orderDetails = new ArrayList<>();

        // Lặp qua danh sách order_details để tạo OrderDetail
        for (OrderDetailRequest orderDetailDto : request.getOrder_details()) {
            Product product = productRepository.findById(orderDetailDto.getProduct_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(orderDetailDto.getQuantity());
            orderDetail.setPrice(product.getPrice() * orderDetailDto.getQuantity());
            orderDetail.setColor(orderDetailDto.getColor());

            // Cộng tổng giá và tổng số lượng sản phẩm
            totalPrice += orderDetail.getPrice();
            totalItem += orderDetail.getQuantity();

            orderDetails.add(orderDetail);
        }

        // Gán danh sách OrderDetail vào Order
        order.setOrderDetails(orderDetails);
        order.setTotalPrice(totalPrice);
        order.setTotalItem(totalItem);

        // Lưu Order vào database (do Cascade.ALL, OrderDetails sẽ tự động lưu)
        Order savedOrder = orderRepository.save(order);

        // Convert Order sang OrderDto và trả về
        return modelMapper.map(savedOrder, OrderDto.class);
    }
}
