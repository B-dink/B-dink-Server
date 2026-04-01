package com.app.bdink.payment.apple.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class AppleReceiptResponse {

    private int status;
    private AppleReceipt receipt;

    @JsonProperty("latest_receipt_info")
    private List<InAppPurchase> latestReceiptInfo;

    @Setter
    private boolean sandbox = false;
}
