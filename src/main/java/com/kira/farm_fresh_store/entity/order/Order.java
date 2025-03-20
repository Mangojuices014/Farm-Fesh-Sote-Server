package com.kira.farm_fresh_store.entity.order;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kira.farm_fresh_store.entity.BaseEntity;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.utils.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tblOrder")
    private List<Transaction> transactionList;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Shipment shipment;

    @Column(name = "business_key", unique = true) // Kiểm tra mapping
    private String businessKey;
    private Integer totalPrice;

    private Integer totalItem;

    private String orderInfo;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreatedDate
    private String completeDate;

    private Boolean send_status; // 0: chưa send, 1: đã send

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
