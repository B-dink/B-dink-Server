package com.app.bdink.payment.apple.dto;

import com.app.bdink.payment.apple.entity.AppleProduct;
import lombok.Builder;

@Builder
public record AppleProductResponse(
        String productId,
        Boolean canPurchase
) {
    public static AppleProductResponse from(AppleProduct product) {
        return AppleProductResponse.builder()
                .productId(product.getProductId())
                .canPurchase(product.getCanPurchase())
                .build();
    }
}
