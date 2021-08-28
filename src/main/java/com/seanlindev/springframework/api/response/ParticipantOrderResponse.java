package com.seanlindev.springframework.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParticipantOrderResponse {
    @JsonProperty("id")
    private String identity;

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

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
