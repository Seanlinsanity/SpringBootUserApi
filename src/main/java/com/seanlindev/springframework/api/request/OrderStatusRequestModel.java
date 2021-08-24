package com.seanlindev.springframework.api.request;

import com.seanlindev.springframework.model.OrderStatus;

public class OrderStatusRequestModel {
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
