package com.kira.farm_fresh_store.request.order;

import com.kira.farm_fresh_store.dto.order.OrderDetailDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private String orderInfo;
    private List<OrderDetailRequest> order_details;
}
