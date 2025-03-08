package com.kira.farm_fresh_store.controller;

import com.kira.farm_fresh_store.dto.order.ShipmentDto;
import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.request.order.CreateShipmentRequest;
import com.kira.farm_fresh_store.request.order.UpdateShipmentRequest;
import com.kira.farm_fresh_store.response.ApiResponse;
import com.kira.farm_fresh_store.service.shipment.IShipmentService;
import com.kira.farm_fresh_store.utils.FeedBackMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final IShipmentService shipmentService;

    @PostMapping("/create-shipment/{orderId}")
    public ResponseEntity<ApiResponse<ShipmentDto>> createShipment(
            @RequestBody CreateShipmentRequest request,
            @PathVariable String orderId) {
        try {
            ShipmentDto shipmentApi = shipmentService.createShipment(request, orderId);
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(CREATED)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, shipmentApi));
        }catch (ResourceNotFoundException e){
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @PutMapping("/update-shipment/{orderId}")
    public ResponseEntity<ApiResponse<ShipmentDto>> updateShipment(
            @PathVariable String orderId,
            @RequestBody UpdateShipmentRequest request) {
        try {
            ShipmentDto shipmentApi = shipmentService.updateShipmentStatus(orderId, request);
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(OK)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, shipmentApi));
        }catch (ResourceNotFoundException e){
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/get-shipment/{orderId}/by-order")
    public ResponseEntity<ApiResponse<ShipmentDto>> getShipmentByIdOrder(@PathVariable String orderId) {
        try{
            ShipmentDto shipmentApi = shipmentService.getShipmentByOrderId(orderId);
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(CREATED)
                    .body(new ApiResponse<>(FeedBackMessage.CREATE_USER_SUCCESS, shipmentApi));
        }catch (ResourceNotFoundException e){
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(CREATED)
                    .body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete-shipment/{shipmentId}")
    public ResponseEntity<ApiResponse<?>> deleteShipment(@PathVariable String shipmentId) {
        try{
            shipmentService.deleteShipment(shipmentId);
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(NO_CONTENT)
                    .body(new ApiResponse<>(FeedBackMessage.SUCCESS, true));
        }catch (ResourceNotFoundException e){
            // Trả về ApiResponse với thông báo thành công
            return ResponseEntity.status(BAD_REQUEST)
                    .body(new ApiResponse<>(FeedBackMessage.UN_SUCCESS, false));
        }
    }

}
