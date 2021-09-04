package com.seanlindev.springframework.services;

import com.seanlindev.springframework.api.dto.OrderDto;
import com.seanlindev.springframework.api.dto.ParticipantOrderDto;
import com.seanlindev.springframework.api.dto.ShipmentDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto order);
    List<OrderDto> getOrdersByOwnerId(String ownerId);
    OrderDto findByOrderId(String orderId);
    OrderDto updateOrderParticipants(ParticipantOrderDto participantOrderDto);
    OrderDto changeOrderStatus(OrderDto orderDto);
    Boolean  deleteOrderByOrderId(String orderId);
    ShipmentDto getOrderShipmentDetails(String orderId);
}
