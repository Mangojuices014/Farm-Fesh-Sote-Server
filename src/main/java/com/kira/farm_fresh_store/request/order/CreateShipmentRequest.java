package com.kira.farm_fresh_store.request.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateShipmentRequest {
    private String order_code;
    private String address;
    private String phone;
    private String email;
    private String customerName;
}
