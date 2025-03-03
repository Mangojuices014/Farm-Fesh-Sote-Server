package com.kira.farm_fresh_store.request.product;
import com.kira.farm_fresh_store.entity.product.*;
import com.kira.farm_fresh_store.utils.enums.ETypeProduct;
import jakarta.persistence.*;
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
public class CreateProductRequest {
    private ETypeProduct type;
    private String name;
    private String origin;
    private LocalDateTime harvestDate;
    private Integer shelfLife;
    private String description;
    private Double weight;
    private Double price;
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
