package com.kira.farm_fresh_store.dto.product;

import camundajar.impl.scala.Int;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kira.farm_fresh_store.entity.product.Image;
import com.kira.farm_fresh_store.utils.enums.ETypeProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private String id;
    private String sku;
    private ETypeProduct type;
    private String origin;
    private LocalDateTime harvestDate;
    private Integer shelfLife;
    private String description;
    private Double price;
    private Integer quantityProduct;
    private Double weight;
    private String image;
    private boolean active;
    private List<Image> images; // 1, 2, 3
    //	----------------	FRUIT   	--------------------
    private Integer sweetnessLevel;
    private String vitaminFruit;
    //	----------------	Vegetable   	--------------------
    private String vitaminVegetable;
    private Boolean leafy;
    //	----------------	Meat   	--------------------
    private Double fatPercentage;
    private Double proteinContent;
    private String meatType;
    //	----------------	Fish   	--------------------
    private Double omega3Content;
    private String waterType;
}
