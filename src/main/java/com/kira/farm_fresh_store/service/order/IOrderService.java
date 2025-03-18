package com.kira.farm_fresh_store.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kira.farm_fresh_store.dto.order.OrderDto;
import com.kira.farm_fresh_store.request.order.CreateFormOrderRequest;
import com.kira.farm_fresh_store.request.order.CreateOrderRequest;

import java.util.List;

public interface IOrderService {
    OrderDto createOrderWithCart(CreateOrderRequest request) throws JsonProcessingException;
    OrderDto createOrder(CreateFormOrderRequest request) throws JsonProcessingException;
    OrderDto getOrderById(String orderId);
    List<OrderDto> getAllOrder();
}
