package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.dto.product.ProductDto;
import com.kira.farm_fresh_store.entity.product.Product;
import com.kira.farm_fresh_store.utils.enums.ETypeProduct;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    Product findFirstByOrderByIdDesc();
    @EntityGraph(attributePaths = {"fruit", "vegetable", "meat", "fish"})
    Optional<Product> findById(String id);
    List<Product> findByType(ETypeProduct type);
}
