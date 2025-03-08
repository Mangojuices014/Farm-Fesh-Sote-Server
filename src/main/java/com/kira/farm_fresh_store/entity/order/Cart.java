package com.kira.farm_fresh_store.entity.order;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kira.farm_fresh_store.entity.BaseEntity;
import com.kira.farm_fresh_store.entity.product.Product;
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
@Table(name="php_carts")
public class Cart extends BaseEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer selected;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
