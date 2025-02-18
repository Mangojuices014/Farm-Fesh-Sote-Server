package com.kira.farm_fresh_store.service.order;

import com.kira.farm_fresh_store.dto.order.OrderDto;
import com.kira.farm_fresh_store.request.order.CreateOrderRequest;

public interface IOrderService {
    OrderDto createOrder(CreateOrderRequest request);
}
