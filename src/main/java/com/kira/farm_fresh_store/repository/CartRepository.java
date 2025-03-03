package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.order.Cart;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, String> {
    Cart findFirstByOrderByIdDesc();
    @Query("SELECT c FROM Cart c WHERE c.product.id = :productId AND c.user.id = :userId")
    Cart findByProductAndUser(@Param("productId") String productId, @Param("userId") Long userId);
}
