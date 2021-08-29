package com.seanlindev.springframework.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsRequestModel {
    private String title;
    private Integer quantity;
    private String owner;
    private List<String> items = new ArrayList<>();

    @JsonProperty("participant_orders")
    private List<ParticipantOrderRequestModel> participants = new ArrayList<>();

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

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
