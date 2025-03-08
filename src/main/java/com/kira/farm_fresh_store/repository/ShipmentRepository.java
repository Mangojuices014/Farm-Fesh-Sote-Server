package com.kira.farm_fresh_store.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.kira.farm_fresh_store.dto.order.ShipmentDto;
import com.kira.farm_fresh_store.entity.order.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, String> {
    Optional<Shipment> findByOrderId(String orderId);
}
