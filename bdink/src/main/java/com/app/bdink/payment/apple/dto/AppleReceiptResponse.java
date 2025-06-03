package com.app.bdink.payment.apple.dto;

import lombok.Getter;

@Getter
public class AppleReceiptResponse {

    private int status;
    private AppleReceipt receipt;
}
