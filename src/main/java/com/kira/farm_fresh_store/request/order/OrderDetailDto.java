package com.kira.farm_fresh_store.request.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kira.farm_fresh_store.entity.order.Order;
import com.kira.farm_fresh_store.entity.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {
    private String productId;
    private Integer quantity;
    private Long price;
    private Long totalPrice;
}
