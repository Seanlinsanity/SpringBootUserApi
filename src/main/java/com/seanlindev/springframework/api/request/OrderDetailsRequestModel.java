package com.seanlindev.springframework.api.request;

import com.seanlindev.springframework.api.dto.OrderDto;
import com.seanlindev.springframework.api.dto.UserDto;
import org.aspectj.weaver.ast.Or;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDetailsRequestModel {
    private String productName;
    private Integer quantity;
    private String owner;
    private List<String> participants = new ArrayList<>();

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public OrderDto convertToOrderDto() {
        OrderDto orderDto = new OrderDto();
        orderDto.setProductName(this.productName);
        orderDto.setQuantity(this.quantity);

        UserDto owner = new UserDto();
        owner.setUserId(this.getOwner());
        orderDto.setOwner(owner);

        List<UserDto> participants = this.getParticipants().stream().map(userId -> {
            UserDto user = new UserDto();
            user.setUserId(userId);
            return user;
        }).collect(Collectors.toList());
        orderDto.setParticipants(participants);
        return orderDto;
    }
}
