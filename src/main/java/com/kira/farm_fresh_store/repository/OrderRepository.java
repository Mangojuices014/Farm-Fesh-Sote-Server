package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
    Order findFirstByOrderByIdDesc();
}
