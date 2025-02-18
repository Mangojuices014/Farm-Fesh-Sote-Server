package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
    Product findFirstByOrderByIdDesc();
}
