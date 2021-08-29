package com.seanlindev.springframework.api.request;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsRequestModel {
    private String productName;
    private Integer quantity;
    private String owner;
    private List<ParticipantOrderRequestModel> participants = new ArrayList<>();
    private List<OrderItemRequestModel> items = new ArrayList<>();

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

    public List<ParticipantOrderRequestModel> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantOrderRequestModel> participants) {
        this.participants = participants;
    }

    public List<OrderItemRequestModel> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequestModel> items) {
        this.items = items;
    }
}
