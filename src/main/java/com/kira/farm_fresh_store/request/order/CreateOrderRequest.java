package com.kira.farm_fresh_store.request.order;

import com.kira.farm_fresh_store.entity.order.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private String orderInfo;
    private CreateShipmentRequest shipment;
    private List<OrderDetailRequest> details;
    @Nullable
    private String transactionNo;
}
