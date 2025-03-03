package com.kira.farm_fresh_store.entity.order;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kira.farm_fresh_store.entity.BaseEntity;
import com.kira.farm_fresh_store.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="php_order")
public class Order extends BaseEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Shipment shipment;

    private Long totalPrice;

    private Integer totalItem;

    private String orderInfo;

    private Integer status;

    private String completeDate;

    private Boolean send_status; // 0: chưa send, 1: đã send

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

}
