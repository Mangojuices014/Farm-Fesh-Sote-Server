package com.kira.farm_fresh_store.dto.order;

import com.kira.farm_fresh_store.entity.order.OrderDetail;
import com.kira.farm_fresh_store.utils.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private String id;
    private String productId;
    private String name;
    private Double price;
    private String image;
    private Integer quantity;
    private Integer selected;
}
