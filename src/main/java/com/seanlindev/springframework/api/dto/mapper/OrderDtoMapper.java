package com.seanlindev.springframework.api.dto.mapper;

import com.seanlindev.springframework.api.dto.OrderDto;
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

        List<UserDto> participants = orderDetailsRequestModel.getParticipants().stream().map(userId -> {
            UserDto user = new UserDto();
            user.setUserId(userId);
            return user;
        }).collect(Collectors.toList());
        orderDto.setParticipants(participants);
        return orderDto;
    }
}
