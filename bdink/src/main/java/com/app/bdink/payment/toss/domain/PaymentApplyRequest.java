package com.app.bdink.payment.toss.domain;

public record PaymentApplyRequest(
        String orderId,
        String amount,
        String paymentKey
) {
}
