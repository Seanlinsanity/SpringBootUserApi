package com.seanlindev.springframework.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seanlindev.springframework.model.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderDto {
    private String id;
    private String orderId;
    private String productName;
    private Integer quantity;
    private UserDto owner;
//    private List<UserDto> participants = new ArrayList<>();

    @JsonProperty("participant_orders")
    private List<ParticipantOrderDto> participantOrders = new ArrayList<>();
    
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

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

//    public List<UserDto> getParticipants() {
//        return participants;
//    }
//
//    public void setParticipants(List<UserDto> participants) {
//        this.participants = participants;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<ParticipantOrderDto> getParticipantOrders() {
        return participantOrders;
    }

    public void setParticipantOrders(List<ParticipantOrderDto> participantOrders) {
        this.participantOrders = participantOrders;
    }
}
