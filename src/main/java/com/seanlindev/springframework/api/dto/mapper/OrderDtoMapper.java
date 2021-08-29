package com.seanlindev.springframework.api.dto.mapper;

import com.seanlindev.springframework.api.dto.OrderDto;
import com.seanlindev.springframework.api.dto.OrderItemDto;
import com.seanlindev.springframework.api.dto.ParticipantOrderDto;
import com.seanlindev.springframework.api.dto.UserDto;
import com.seanlindev.springframework.api.request.OrderDetailsRequestModel;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDtoMapper {
    public static OrderDto convertToOrderDto(OrderDetailsRequestModel orderDetailsRequestModel) {
        OrderDto orderDto = new OrderDto();
        orderDto.setProductName(orderDetailsRequestModel.getProductName());
        orderDto.setQuantity(orderDetailsRequestModel.getQuantity());

        UserDto owner = new UserDto();
        owner.setUserId(orderDetailsRequestModel.getOwner());
        orderDto.setOwner(owner);

        List<ParticipantOrderDto> participantOrders = orderDetailsRequestModel.getParticipants().stream().map(orderParticipant -> {
            ParticipantOrderDto participantOrderDto = new ParticipantOrderDto();
            participantOrderDto.setUserId(orderParticipant.getUserId());
            participantOrderDto.setQuantity(orderParticipant.getQuantity());
            return participantOrderDto;
        }).collect(Collectors.toList());
        orderDto.setParticipantOrders(participantOrders);

        List<OrderItemDto> orderItems = orderDetailsRequestModel.getItems().stream().map(item -> {
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setProductId(item.getProductId());
            return orderItemDto;
        }).collect(Collectors.toList());
        orderDto.setItems(orderItems);
        return orderDto;
    }
}
