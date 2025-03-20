package com.kira.farm_fresh_store.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kira.farm_fresh_store.dto.order.OrderDto;
import com.kira.farm_fresh_store.entity.order.Cart;
import com.kira.farm_fresh_store.entity.order.Order;
import com.kira.farm_fresh_store.entity.order.OrderDetail;
import com.kira.farm_fresh_store.entity.order.Shipment;
import com.kira.farm_fresh_store.entity.product.Product;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.exception.UserNotAuthenticatedException;
import com.kira.farm_fresh_store.repository.*;
import com.kira.farm_fresh_store.request.order.CreateFormOrderRequest;
import com.kira.farm_fresh_store.request.order.CreateOrderRequest;
import com.kira.farm_fresh_store.request.order.OrderDetailRequest;
import com.kira.farm_fresh_store.utils.AuthenUtil;
import com.kira.farm_fresh_store.utils.Util;
import com.kira.farm_fresh_store.utils.enums.Status;
import jakarta.transaction.Transactional;
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

import static org.camunda.bpm.admin.impl.plugin.resources.MetricsRestService.objectMapper;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final ModelMapper modelMapper;

    private final ProductRepository productRepository;

    private final CartRepository cartRepository;

    private final UserRepository userRepository;

    private final ShipmentRepository shipmentRepository;

    private final RuntimeService runtimeService;

    private final Util util;

    private final TaskService taskService;

    @Override
    @Transactional
    public OrderDto createOrderWithCart(CreateOrderRequest request) throws JsonProcessingException {
        // 1. Khởi chạy quy trình Camunda
        String businessKey = util.generateRandomID();
        // Lấy thông tin User
        long userId = AuthenUtil.getProfileId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng."));

        // Lấy danh sách sản phẩm từ giỏ hàng của người dùng
        List<Cart> cartItems = cartRepository.findByUserAndSelected(user, 1);
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng trống. Không thể tạo đơn hàng.");
        }
        // Tạo đơn hàng mới
        Order order = new Order();
        Order lastOrder = orderRepository.findFirstByOrderByIdDesc();
        if (lastOrder == null) {
            order.setId(util.createNewID("ORDER"));
        } else {
            order.setId(util.createIDFromLastID("ORDER", 5, lastOrder.getId()));
        }
        order.setUser(user);
        order.setOrderInfo(request.getOrderInfo());
        order.setStatus(Status.WAITING);
        order.setSend_status(false);
        order.setBusinessKey(businessKey);

        // Tạo danh sách OrderDetail từ Cart
        List<OrderDetail> orderDetails = new ArrayList<>();
        int totalPrice = 0;
        int totalItem = 0;

        for (Cart cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getProduct().getPrice());
            orderDetail.setTotalPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());

            orderDetails.add(orderDetail);
            totalPrice += orderDetail.getTotalPrice();
            totalItem += cartItem.getQuantity();
        }
        // Gán OrderDetail vào Order

        order.setOrderDetails(orderDetails);
        order.setTotalItem(totalItem);
        order.setTotalPrice(totalPrice);

        // Tạo Shipment đi kèm
        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setAddress(request.getShipment().getAddress());
        shipment.setPhone(request.getShipment().getPhone());
        shipment.setEmail(request.getShipment().getEmail());
        shipment.setCustomerName(request.getShipment().getCustomerName());
        shipment.setOrder_code(util.generateRandomID());

        // Gán Shipment vào Order
        order.setShipment(shipment);

        // Lưu vào database
        Order savedOrder = orderRepository.save(order);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "orderPorocessPayment", businessKey);
        // 2. Lấy task "Kiểm tra đăng nhập"
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        // 3. Kiểm tra đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
        Map<String, Object> variables = new HashMap<>();
        List<String> productIds = orderDetails.stream()
                .map(orderDetail -> orderDetail.getProduct().getId())
                .toList();
        variables.put("productIds", productIds);
        variables.put("auth", isAuthenticated);
        variables.put("businessKey", businessKey);
        // Nếu có task thì hoàn thành nó với biến auth
        if (task != null) {
            taskService.complete(task.getId(), variables);
        }

        // 4. Nếu không đăng nhập, ném lỗi
        if (!isAuthenticated) {
            throw new UserNotAuthenticatedException("Bạn vẫn chưa đăng nhập");
        }

        return modelMapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public OrderDto createOrder(CreateFormOrderRequest request){
        // 1. Khởi chạy quy trình Camunda
        String businessKey = util.generateRandomID();

        // Lấy thông tin User
        long userId = AuthenUtil.getProfileId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng."));

        // Tạo đơn hàng mới
        Order order = new Order();
        Order lastOrder = orderRepository.findFirstByOrderByIdDesc();
        if (lastOrder == null) {
            order.setId(util.createNewID("ORDER"));
        } else {
            order.setId(util.createIDFromLastID("ORDER", 5, lastOrder.getId()));
        }
        order.setUser(user);
        order.setOrderInfo(request.getOrderInfo());
        order.setStatus(Status.WAITING);
        order.setSend_status(false);
        order.setBusinessKey(businessKey);

        // Xử lý OrderDetail từ request
        OrderDetailRequest orderDetailDto = request.getDetails();
        Product product = productRepository.findById(orderDetailDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setQuantity(orderDetailDto.getQuantity());
        orderDetail.setPrice(product.getPrice());
        orderDetail.setTotalPrice(product.getPrice() * orderDetailDto.getQuantity());

        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails.add(orderDetail);

        // Gán OrderDetail vào Order
        order.setOrderDetails(orderDetails);
        order.setTotalItem(orderDetail.getQuantity());
        order.setTotalPrice(orderDetail.getPrice());

        // Tạo Shipment đi kèm
        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setAddress(request.getShipment().getAddress());
        shipment.setPhone(request.getShipment().getPhone());
        shipment.setEmail(request.getShipment().getEmail());
        shipment.setCustomerName(request.getShipment().getCustomerName());
        shipment.setOrder_code(util.generateRandomID());

        // Gán Shipment vào Order
        order.setShipment(shipment);

        // Lưu vào database
        Order savedOrder = orderRepository.save(order);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "orderPorocessPayment", businessKey);

        // 2. Lấy task "Kiểm tra đăng nhập"
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();

        // 3. Kiểm tra đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
        Map<String, Object> variables = new HashMap<>();

        String productId = orderDetail.getProduct().getId();
        variables.put("productIds", List.of(productId)); // Tạo danh sách chứa 1 phần tử
        variables.put("auth", isAuthenticated);
        variables.put("businessKey", businessKey);

        // Nếu có task thì hoàn thành nó với biến auth
        if (task != null) {
            taskService.complete(task.getId(), variables);
        }

        // 4. Nếu không đăng nhập, ném lỗi
        if (!isAuthenticated) {
            throw new UserNotAuthenticatedException("Bạn vẫn chưa đăng nhập");
        }

        return modelMapper.map(savedOrder, OrderDto.class);
    }


    @Override
    public OrderDto getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn đặt hàng"));
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public List<OrderDto> getAllOrder() {
        // ✅ Đổi kiểu trả về thành List<OrderDto>
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList()); // ✅ Trả về List<OrderDto>
    }

}
