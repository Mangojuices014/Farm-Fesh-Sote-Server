package com.kira.farm_fresh_store.entity.product;

import com.kira.farm_fresh_store.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="products")
public class Product extends BaseEntity {
    @Id
    private String id;
    private String sku;
    private String name;
    private String description;
    private Double price;
    private Integer quantityProduct;
    @Column(name = "photo", columnDefinition = "TEXT")
    private String image;
    private boolean active;
    private String material;
    private String sizeWeight;
    private Integer weight; // -- Bắt buộc, khối lượng đóng gói
    private Integer length; // -- Bắt buộc, chiều dài khi đóng gói
    private Integer width; // -- Bắt buộc, chiều rộng khi đóng gói
    private Integer height;
}
