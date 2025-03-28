package com.app.bdink.payment.domain;

public record PaymentApplyRequest(
        String orderId,
        String amount,
        String paymentKey
) {
}
