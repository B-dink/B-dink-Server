package com.app.bdink.payment.controller.dto;

public record ConfirmRequest(
    String paymentKey,
    String orderId,
    int amount
) {

}
