package com.app.bdink.payment.apple;

import lombok.Builder;

@Builder
public record PurchaseRequest(
        String transactionId,
        String appStoreReceipt,
        String productId
) {
    public static PurchaseRequest of(String transactionId, String appStoreReceipt, String productId) {
        return PurchaseRequest.builder()
                .transactionId(transactionId)
                .appStoreReceipt(appStoreReceipt)
                .productId(productId)
                .build();
    }
}
