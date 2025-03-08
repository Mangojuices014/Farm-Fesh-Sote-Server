package com.kira.farm_fresh_store.dto.order;

import com.kira.farm_fresh_store.request.order.OrderDetailDto;
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
    private Long userId;
    private String shipmentId;
    private Long totalPrice;
    private Integer totalItem;
    private String orderInfo;
    private String status;
    private String completeDate;
    private Boolean send_status;
    private List<OrderDetailDto> orderDetails = new ArrayList<>();
}
