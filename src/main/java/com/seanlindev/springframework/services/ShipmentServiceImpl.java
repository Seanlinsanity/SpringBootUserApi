package com.seanlindev.springframework.services;

import com.seanlindev.springframework.api.dto.ShipmentDto;
import com.seanlindev.springframework.providers.ShipmentProvider;
import org.springframework.stereotype.Service;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentProvider shipmentProvider;

    public ShipmentServiceImpl(ShipmentProvider shipmentProvider) {
        this.shipmentProvider = shipmentProvider;
    }

    @Override
    public ShipmentDto createShipment(ShipmentDto shipmentDto) throws Exception {
        try {
            return shipmentProvider.requestCreateShipment(shipmentDto);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ShipmentDto getShipmentDetails(String shipmentId) throws Exception {
        try {
            return shipmentProvider.getShipmentDetails(shipmentId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ShipmentDto cancelShipment(String shipmentId) throws Exception {
        try {
            return shipmentProvider.cancelShipment(shipmentId);
        } catch (Exception e) {
            throw e;
        }
    }
}
