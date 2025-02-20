package com.kira.farm_fresh_store.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {
    private String id;
    private Integer quantityOrder;
    private Double price;
    private String color;
    private String productId;
}
