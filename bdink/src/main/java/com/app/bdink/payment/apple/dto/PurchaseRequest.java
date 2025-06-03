package com.app.bdink.payment.apple.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PurchaseRequest(
        @NotBlank String transactionId,
        @NotBlank String appStoreReceipt,
        @NotBlank String productId
) {
    public static PurchaseRequest of(String transactionId, String appStoreReceipt, String productId) {
        return PurchaseRequest.builder()
                .transactionId(transactionId)
                .appStoreReceipt(appStoreReceipt)
                .productId(productId)
                .build();
    }
}
