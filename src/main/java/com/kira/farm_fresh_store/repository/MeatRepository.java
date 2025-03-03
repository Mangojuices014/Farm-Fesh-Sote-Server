package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.product.Meat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeatRepository extends JpaRepository<Meat, String> {
    Meat findFirstByOrderByIdDesc();
}
