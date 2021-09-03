package com.seanlindev.springframework.services;

import com.seanlindev.springframework.api.dto.ShipmentDto;

public interface ShipmentService {
    String createShipment(ShipmentDto shipmentDto) throws Exception;
}
