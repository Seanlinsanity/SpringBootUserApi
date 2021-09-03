package com.seanlindev.springframework.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seanlindev.springframework.model.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderDto {
    private String id;
    private String orderId;
    private String title;
    private Integer quantity;
    private UserDto owner;
    private List<OrderItemDto> items = new ArrayList<>();
    private OrderStatus status;
    private String shipmentId;

    @JsonProperty("participant_orders")
    private List<ParticipantOrderDto> participantOrders = new ArrayList<>();

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

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

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
