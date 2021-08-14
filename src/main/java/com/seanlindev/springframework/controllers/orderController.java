package com.seanlindev.springframework.controllers;

import com.seanlindev.springframework.api.dto.OrderDto;
import com.seanlindev.springframework.api.request.OrderDetailsRequestModel;
import com.seanlindev.springframework.api.request.OrderParticipantsRequestModel;
import com.seanlindev.springframework.api.request.UserDetailsRequestModel;
import com.seanlindev.springframework.api.response.OrderResponse;
import com.seanlindev.springframework.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("orders")
public class orderController {

    OrderService orderService;

    public orderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse createNewOrder(@RequestBody OrderDetailsRequestModel orderDetails) throws Exception {
        OrderDto orderDto = orderService.createOrder(orderDetails.convertToOrderDto());
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(orderDto, OrderResponse.class);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable String id) {
        OrderDto orderDto = orderService.findByOrderId(id);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(orderDto, OrderResponse.class);
    }

    @PutMapping("/{id}/participants")
    public OrderResponse updateOrderParticipants(@PathVariable String id,
                                                 @RequestBody OrderParticipantsRequestModel orderParticipantsRequestModel) throws Exception {
        OrderDto orderDto = orderService.updateOrderParticipant(id,
                                                                orderParticipantsRequestModel.getParticipantId(),
                                                                orderParticipantsRequestModel.getQuantity());
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(orderDto, OrderResponse.class);
    }
}
