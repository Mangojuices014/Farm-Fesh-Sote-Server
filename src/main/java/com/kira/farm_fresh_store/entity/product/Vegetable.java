package com.kira.farm_fresh_store.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="php_vegetable")
public class Vegetable {
    @Id
    private String id;
    @OneToOne
    @JoinColumn(name = "product_id", unique = true)
    private Product product;
    private String vitaminVegetable;
    private Boolean leafy;
}
