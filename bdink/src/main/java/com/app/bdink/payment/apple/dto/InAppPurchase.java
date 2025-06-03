package com.app.bdink.payment.apple.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class InAppPurchase {

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("original_transaction_id")
    private String originalTransactionId;

    @JsonProperty("purchase_date_ms")
    private String purchaseDateMs;
}
