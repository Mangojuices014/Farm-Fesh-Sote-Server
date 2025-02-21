package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.order.Order;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Order findFirstByOrderByIdDesc();

    @Query("SELECT o FROM Order o WHERE o.bussinessKey = :businessKey")
    Optional<Order> findByBusinessKey(@Param("businessKey") String businessKey);
}
