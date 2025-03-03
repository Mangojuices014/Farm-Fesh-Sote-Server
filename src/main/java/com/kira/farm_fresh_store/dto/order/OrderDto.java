package com.kira.farm_fresh_store.dto.order;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kira.farm_fresh_store.entity.order.OrderDetail;
import com.kira.farm_fresh_store.entity.order.Payment;
import com.kira.farm_fresh_store.entity.order.Shipment;
import com.kira.farm_fresh_store.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String id;
    private String userId;
    private String paymentId;
    private String shipmentId;
    private Long totalPrice;
    private Integer totalItem;
    private String orderInfo;
    private Integer status;
    private String completeDate;
    private Boolean send_status;
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
