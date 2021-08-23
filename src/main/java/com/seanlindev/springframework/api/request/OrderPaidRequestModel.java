package com.seanlindev.springframework.api.request;

public class OrderPaidRequestModel {
    private boolean isPaid;

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
