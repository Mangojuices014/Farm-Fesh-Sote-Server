package com.kira.farm_fresh_store.dto.order;

import com.kira.farm_fresh_store.entity.BaseEntity;
import com.kira.farm_fresh_store.entity.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDto {
    private String id;
    private String orderId;
    private String order_code;
    private String address;
    private String phone;
    private String email;
    private String customerName;
}
