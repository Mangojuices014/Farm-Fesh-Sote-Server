package com.kira.farm_fresh_store.request.product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateProductRequest {
    private String name;
    private String description;
    private Double price;
    private int quantity;
    private String image;
    private String material;
    private String sizeWeight;
    private Integer weight;
    private Integer length;
    private Integer width;
    private Integer height;
}
