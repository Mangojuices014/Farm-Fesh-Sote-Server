package com.kira.farm_fresh_store.entity.product;

import com.kira.farm_fresh_store.entity.BaseEntity;
import com.kira.farm_fresh_store.utils.enums.ETypeProduct;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "php_products")
public class Product extends BaseEntity {
    @Id
    private String id;
    private String sku;

    @Enumerated(EnumType.STRING)
    private ETypeProduct type;
    private String name;
    private String origin;
    private LocalDateTime harvestDate;
    private Integer shelfLife;
    private String description;
    private Integer price;
    private Integer quantityProduct;
    private Double weight;

    @Column(name = "photo", columnDefinition = "TEXT")
    private String image;
    private Boolean active;

    // Quan hệ với các thực thể con
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Fruit fruit;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Vegetable vegetable;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Meat meat;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Fish fish;
}

