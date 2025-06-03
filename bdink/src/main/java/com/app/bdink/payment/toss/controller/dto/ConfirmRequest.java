package com.app.bdink.payment.toss.controller.dto;

public record ConfirmRequest(
    String paymentKey,
    String orderId,
    int amount
) {

}
