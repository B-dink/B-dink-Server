package com.app.bdink.payment.apple.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationResult {

    private final boolean valid;
    private final String errorMessage;
    private final InAppPurchase inAppPurchase;

    public static ValidationResult success(InAppPurchase inAppPurchase) {
        return new ValidationResult(true, null, inAppPurchase);
    }

    public static ValidationResult fail(String errorMessage) {
        return new ValidationResult(false, errorMessage, null);
    }

    public String getTransactionId() {
        return inAppPurchase != null ? inAppPurchase.getTransactionId() : null;
    }
}
