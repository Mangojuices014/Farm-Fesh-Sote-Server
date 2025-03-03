package com.kira.farm_fresh_store.entity.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kira.farm_fresh_store.entity.BaseEntity;
import com.kira.farm_fresh_store.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="php_order_detail")
public class OrderDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    private Long price;

    private Long totalPrice;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

}
