package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.product.Vegetable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VegetableRepository extends JpaRepository<Vegetable, String> {
    Vegetable findFirstByOrderByIdDesc();
}
