package com.app.bdink.payment.apple.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PurchaseResponse {

    private String status;  // "SUCCESS" or "FAIL"
    private String message;
    private String transactionId;
}
