package com.seanlindev.springframework.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seanlindev.springframework.api.dto.OrderItemDto;
import com.seanlindev.springframework.model.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderResponse {
    private String orderId;
    private String title;
    private Integer quantity;
    private UserResponse owner;
    private List<OrderItemDto> items;
    private OrderStatus status;
    private String shipmentId;

    @JsonProperty("participant_orders")
    private List<ParticipantOrderResponse> participantOrders = new ArrayList<>();

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    public List<ParticipantOrderResponse> getParticipantOrders() {
        return participantOrders;
    }

    public void setParticipantOrders(List<ParticipantOrderResponse> participantOrders) {
        this.participantOrders = participantOrders;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }
}
