package com.kira.farm_fresh_store.dto.product;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {
    private String id;
    private String sku;
    private String name;
    private String description;
    private Double price;
    private int quantity;
    private String image;
    private boolean active;
    private String material;
    private String sizeWeight;
    private Integer weight; // -- Bắt buộc, khối lượng đóng gói
    private Integer length; // -- Bắt buộc, chiều dài khi đóng gói
    private Integer width; // -- Bắt buộc, chiều rộng khi đóng gói
    private Integer height;
}
