package com.kira.farm_fresh_store.request.product;

import com.kira.farm_fresh_store.utils.enums.ETypeProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateProductRequest {
    private String name;
    private String origin;
    private LocalDateTime harvestDate;
    private Integer shelfLife;
    private String description;
    private Double weight;
    private Integer price;
    private Integer quantityProduct;

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
