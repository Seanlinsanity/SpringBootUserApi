package com.seanlindev.springframework.api.response;

import com.seanlindev.springframework.api.dto.UserDto;
import com.seanlindev.springframework.model.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderResponse {
    private String orderId;
    private String productName;
    private Integer quantity;
    private UserResponse owner;
    private List<UserResponse> participants = new ArrayList<>();
    private OrderStatus status;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

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

    public UserResponse getOwner() {
        return owner;
    }

    public void setOwner(UserResponse owner) {
        this.owner = owner;
    }

    public List<UserResponse> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserResponse> participants) {
        this.participants = participants;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus orderStatus) {
        this.status = status;
    }
}
