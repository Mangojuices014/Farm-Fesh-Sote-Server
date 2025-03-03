package com.kira.farm_fresh_store.repository;

import com.kira.farm_fresh_store.entity.order.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, String> {
}
