package com.app.bdink.payment.apple;

import lombok.Builder;

@Builder
public record NewPurchaseRequest(
        String transactionId,
        String appStoreReceipt,
        String productId) {
    public static NewPurchaseRequest of(String transactionId, String appStoreReceipt, String productId) {
        return NewPurchaseRequest.builder()
                .transactionId(transactionId)
                .appStoreReceipt(appStoreReceipt)
                .productId(productId)
                .build();
    }
}
