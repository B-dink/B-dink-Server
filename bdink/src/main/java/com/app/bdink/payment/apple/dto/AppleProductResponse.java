package com.app.bdink.payment.apple.dto;

import com.app.bdink.payment.apple.entity.AppleProduct;
import lombok.Builder;

@Builder
public record AppleProductResponse(
        Long classRoomId,
        String productId,
        Boolean canPurchase
) {
    public static AppleProductResponse from(AppleProduct product) {
        return AppleProductResponse.builder()
                .classRoomId(product.getClassRoom() != null ? product.getClassRoom().getId() : null)
                .productId(product.getProductId())
                .canPurchase(product.getCanPurchase())
                .build();
    }
}
