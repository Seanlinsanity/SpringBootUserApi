package com.seanlindev.springframework.api.request;

public class OrderParticipantsRequestModel {
    private String participantId;
    private Integer quantity;

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
