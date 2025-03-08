package com.kira.farm_fresh_store.service.shipment;

import com.kira.farm_fresh_store.dto.order.ShipmentDto;
import com.kira.farm_fresh_store.request.order.CreateShipmentRequest;
import com.kira.farm_fresh_store.request.order.UpdateShipmentRequest;

import java.util.Optional;

public interface IShipmentService {
    ShipmentDto createShipment(CreateShipmentRequest request,String orderId);
    ShipmentDto updateShipmentStatus(String shipmentId, UpdateShipmentRequest request);  // Cập nhật trạng thái
    ShipmentDto getShipmentByOrderId(String orderId);  // Lấy Shipment theo Order
    void deleteShipment(String shipmentId);  // Xóa shipment (nếu cần)
}
