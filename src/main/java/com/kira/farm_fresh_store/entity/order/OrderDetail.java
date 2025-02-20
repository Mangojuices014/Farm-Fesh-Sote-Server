package com.kira.farm_fresh_store.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kira.farm_fresh_store.entity.product.Product;
import com.kira.farm_fresh_store.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="order_details")
public class OrderDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Integer quantityOrder;
    private Double price;
    private String color;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
