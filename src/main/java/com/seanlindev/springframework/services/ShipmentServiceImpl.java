package com.seanlindev.springframework.services;

import com.seanlindev.springframework.api.dto.ShipmentDto;
import com.seanlindev.springframework.providers.ShipmentServiceProvider;
import org.springframework.stereotype.Service;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentServiceProvider shipmentServiceProvider;

    public ShipmentServiceImpl(ShipmentServiceProvider shipmentServiceProvider) {
        this.shipmentServiceProvider = shipmentServiceProvider;
    }

    @Override
    public String createShipment(ShipmentDto shipmentDto) throws Exception {
        try {
            String shipmentId = shipmentServiceProvider.requestCreateShipment(shipmentDto);
            return shipmentId;
        } catch (Exception e) {
            throw e;
        }
    }
}
