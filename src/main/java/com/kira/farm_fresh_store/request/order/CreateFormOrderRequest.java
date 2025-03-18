package com.kira.farm_fresh_store.request.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateFormOrderRequest {
    private String orderInfo;
    private CreateShipmentRequest shipment;
    private List<OrderDetailRequest> details;
}
