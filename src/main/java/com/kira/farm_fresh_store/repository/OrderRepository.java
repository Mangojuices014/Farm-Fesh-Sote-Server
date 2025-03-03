package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, String> {
    Order findFirstByOrderByIdDesc();

    @Query(value = "SELECT * FROM orders WHERE status = 'PENDING'", nativeQuery = true)
    Optional<List<Order>> findAllOrder();
}
