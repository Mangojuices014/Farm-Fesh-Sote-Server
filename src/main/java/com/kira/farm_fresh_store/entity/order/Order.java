package com.kira.farm_fresh_store.entity.order;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kira.farm_fresh_store.entity.BaseEntity;
import com.kira.farm_fresh_store.entity.user.User;
import com.kira.farm_fresh_store.utils.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="orders")
public class Order extends BaseEntity {
    @Id
    private String id;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "total_item")
    private int totalItem;
    private String orderInfo;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
