package com.kira.farm_fresh_store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kira.farm_fresh_store.dto.order.OrderDto;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.exception.UnauthorizedException;
import com.kira.farm_fresh_store.request.order.CreateOrderRequest;
import com.kira.farm_fresh_store.response.ApiResponse;
import com.kira.farm_fresh_store.service.order.OrderService;
import com.kira.farm_fresh_store.utils.FeedBackMessage;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        }catch (UnauthorizedException e) {
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(new ApiResponse<>(e.getMessage(), null));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-order/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(@PathVariable String orderId) {
        try {
            OrderDto orderAPI = orderService.getOrderById(orderId);
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, orderAPI));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(FeedBackMessage.UN_SUCCESS, null));
        }
    }

    @GetMapping("/get-all-order")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getAllOrder() {
        try {
            List<OrderDto> orderAPI = orderService.getAllOrder();
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, orderAPI));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(FeedBackMessage.UN_SUCCESS, null));
        }
    }

}
