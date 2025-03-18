package com.kira.farm_fresh_store.request.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailRequest {
    private String productId;
    private Integer quantity;
}
