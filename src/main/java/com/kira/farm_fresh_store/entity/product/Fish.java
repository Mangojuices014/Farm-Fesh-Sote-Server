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
@Table(name="php_fish")
public class Fish {
    @Id
    private String id;
    @OneToOne
    @JoinColumn(name = "product_id", unique = true)
    private Product product;

    private Double omega3Content;
    private String waterType;
}
