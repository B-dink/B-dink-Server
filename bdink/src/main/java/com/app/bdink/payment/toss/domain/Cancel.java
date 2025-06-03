package com.app.bdink.payment.toss.domain;

public record Cancel(
        Double cancelAmount,
        String cancelReason,
        Double taxFreeAmount,
        Integer taxExemptionAmount,
        Double refundableAmount,
        Double transferDiscountAmount,
        Double easyPayDiscountAmount,
        String canceledAt,
        String transactionKey,
        String receiptKey,
        String cancelStatus,
        String cancelRequestId
) {
}
