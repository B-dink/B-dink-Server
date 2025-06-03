package com.app.bdink.payment.apple.dto;

import com.app.bdink.payment.apple.entity.AppleProduct;
import com.app.bdink.payment.apple.entity.ApplePurchaseHistory;
import jakarta.persistence.Column;
import lombok.Builder;

@Builder
public record ApplePurchaseHistoryResponse(
        String productId,
        String transactionId
) {
    public static ApplePurchaseHistoryResponse from(ApplePurchaseHistory applePurchaseHistory) {
        return ApplePurchaseHistoryResponse.builder()
                .productId(applePurchaseHistory.getProductId())
                .transactionId(applePurchaseHistory.getTransactionId())
                .build();
    }
}
