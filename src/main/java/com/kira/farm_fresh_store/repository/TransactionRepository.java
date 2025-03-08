package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.order.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Transaction findByTransNo(String transNo);
}
