package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.product.Fish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FishRepository extends JpaRepository<Fish, String> {
    Fish findFirstByOrderByIdDesc();
}
