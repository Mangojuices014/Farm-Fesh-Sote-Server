package com.kira.farm_fresh_store.service.shipment;

import com.kira.farm_fresh_store.dto.order.ShipmentDto;
import com.kira.farm_fresh_store.entity.order.Order;
import com.kira.farm_fresh_store.entity.order.Shipment;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.repository.OrderRepository;
import com.kira.farm_fresh_store.repository.ShipmentRepository;
import com.kira.farm_fresh_store.request.order.CreateShipmentRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipmentService implements IShipmentService {

    private final ShipmentRepository shipmentRepository;

    private final OrderRepository orderRepository;

    private final ModelMapper modelMapper;

    @Override
    public ShipmentDto createShipment(CreateShipmentRequest request,String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tim thấy đơn hàng"));
        Shipment shipment = modelMapper.map(request, Shipment.class);
        shipment.setOrder(order);
        return modelMapper.map(shipmentRepository.save(shipment), ShipmentDto.class);
    }

    @Override
    public ShipmentDto getShipmentById(String id) {
        return null;
    }

    @Override
    public ShipmentDto updateShipment(CreateShipmentRequest shipmentDto, String id) {
        return null;
    }

    @Override
    public ShipmentDto deleteShipment(String id) {
        return null;
    }

    @Override
    public ShipmentDto deleteAllShipment() {
        return null;
    }
}
