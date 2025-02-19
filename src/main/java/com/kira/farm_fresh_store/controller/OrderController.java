package com.kira.farm_fresh_store.controller;

import com.kira.farm_fresh_store.dto.order.OrderDto;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.exception.UserNotAuthenticatedException;
import com.kira.farm_fresh_store.request.order.CreateOrderRequest;
import com.kira.farm_fresh_store.response.ApiResponse;
import com.kira.farm_fresh_store.service.order.OrderService;
import com.kira.farm_fresh_store.utils.FeedBackMessage;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final RuntimeService runtimeService;

    private final OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(@RequestBody CreateOrderRequest orderDto) {
        try {
            
            OrderDto order = orderService.createOrder(orderDto);
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(CREATED)
                    .body(new ApiResponse<>(FeedBackMessage.CREATE_USER_SUCCESS, order));
        } catch (ResourceNotFoundException e) {
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }catch (UserNotAuthenticatedException e) {
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

}
