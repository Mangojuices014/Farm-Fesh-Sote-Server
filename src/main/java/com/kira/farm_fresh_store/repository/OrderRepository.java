package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.order.Order;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, String> {
    Order findFirstByOrderByIdDesc();

    @Query(value = "SELECT * FROM orders WHERE status = 'PENDING'", nativeQuery = true)
    Optional<List<Order>> findAllOrder();

    @Query("SELECT o.totalItem FROM Order o WHERE o.businessKey = :bussinesskey")
    Integer findTotalItemByBussinesskey(@Param("bussinesskey") String bussinesskey);

    Optional<Order>findByBusinessKey(String businessKey);

}
