package com.app.bdink.payment;

public record ConfirmRequest(
    String paymentKey,
    String orderId,
    int amount
) {

}
