package com.kira.farm_fresh_store.service.shipment;

import com.kira.farm_fresh_store.dto.order.ShipmentDto;
import com.kira.farm_fresh_store.request.order.CreateShipmentRequest;

public interface IShipmentService {
    ShipmentDto createShipment(CreateShipmentRequest request,String orderId);
    ShipmentDto getShipmentById(String id);
    ShipmentDto updateShipment(CreateShipmentRequest request, String id);
    ShipmentDto deleteShipment(String id);
    ShipmentDto deleteAllShipment();
}
