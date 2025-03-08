package com.kira.farm_fresh_store.service.shipment;

import com.google.api.client.googleapis.util.Utils;
import com.kira.farm_fresh_store.dto.order.ShipmentDto;
import com.kira.farm_fresh_store.entity.order.Order;
import com.kira.farm_fresh_store.entity.order.Shipment;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.OrderRepository;
import com.kira.farm_fresh_store.repository.ShipmentRepository;
import com.kira.farm_fresh_store.request.order.CreateShipmentRequest;
import com.kira.farm_fresh_store.request.order.UpdateShipmentRequest;
import com.kira.farm_fresh_store.utils.Util;
import com.kira.farm_fresh_store.utils.enums.Status;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShipmentService implements IShipmentService {

    private final ShipmentRepository shipmentRepository;

    private final OrderRepository orderRepository;

    private final ModelMapper modelMapper;

    private final Util util;

    @Override
    public ShipmentDto createShipment(CreateShipmentRequest request, String orderId) {
        // Chuyển orderId từ String -> Long (nếu cần)
        Long orderIdLong = Long.parseLong(orderId);
        // Tìm đơn hàng, nếu không có thì ném lỗi
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với ID: " + orderId));
        // Chuyển đổi từ DTO -> Entity
        Shipment shipment = modelMapper.map(request, Shipment.class);
        shipment.setOrder(order);
        shipment.setOrder_code(util.generateRandomID());
        // Lưu vào database
        Shipment savedShipment = shipmentRepository.save(shipment);
        // Chuyển đổi từ Entity -> DTO
        return modelMapper.map(savedShipment, ShipmentDto.class);
    }

    @Override
    public ShipmentDto updateShipmentStatus(String shipmentId, UpdateShipmentRequest request) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow((() -> new ResourceNotFoundException("Không tìm thấy địa chỉ giao hàng")));
        shipment.setAddress(request.getAddress());
        shipment.setPhone(request.getPhone());
        shipment.setEmail(request.getEmail());
        shipment.setCustomerName(request.getCustomerName());
        return modelMapper.map(shipmentRepository.save(shipment), ShipmentDto.class);
    }

    @Override
    public ShipmentDto getShipmentByOrderId(String orderId) {
        Shipment shipment = shipmentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Shipment cho Order ID: " + orderId));
        return modelMapper.map(shipment, ShipmentDto.class);
    }


    @Override
    public void deleteShipment(String shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow((() -> new ResourceNotFoundException("Không tìm thấy địa chỉ giao hàng")));
        shipmentRepository.delete(shipment);
    }
}
