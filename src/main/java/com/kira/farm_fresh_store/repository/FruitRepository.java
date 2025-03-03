package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.product.Fruit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FruitRepository extends JpaRepository<Fruit, String> {
    Fruit findFirstByOrderByIdDesc();
}
