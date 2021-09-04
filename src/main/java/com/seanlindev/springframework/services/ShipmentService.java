package com.seanlindev.springframework.services;

import com.seanlindev.springframework.api.dto.ShipmentDto;

public interface ShipmentService {
    ShipmentDto createShipment(ShipmentDto shipmentDto) throws Exception;
    ShipmentDto getShipmentDetails(String shipmentId) throws Exception;
    ShipmentDto cancelShipment(String shipmentId) throws Exception;
}
