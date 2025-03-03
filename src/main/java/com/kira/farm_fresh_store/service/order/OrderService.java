package com.kira.farm_fresh_store.service.order;

import com.kira.farm_fresh_store.repository.OrderRepository;
import com.kira.farm_fresh_store.repository.ProductRepository;
import com.kira.farm_fresh_store.repository.UserRepository;
import com.kira.farm_fresh_store.utils.Util;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

//    @Override
//    public OrderDto createOrder(CreateOrderRequest request) {
//
//        return null;
//    }

}
