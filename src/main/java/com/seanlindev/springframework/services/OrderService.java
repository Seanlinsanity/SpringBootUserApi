package com.seanlindev.springframework.services;

import com.seanlindev.springframework.api.dto.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto order);
    List<OrderDto> getOrdersByOwnerId(String ownerId);
    OrderDto findByOrderId(String orderId);
}
