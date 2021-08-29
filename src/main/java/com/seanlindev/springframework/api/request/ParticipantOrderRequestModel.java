package com.seanlindev.springframework.api.request;

public class ParticipantOrderRequestModel {
    private String userId;
    private Integer quantity;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
