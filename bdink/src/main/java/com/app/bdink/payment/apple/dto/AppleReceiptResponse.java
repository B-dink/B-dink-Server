package com.app.bdink.payment.apple.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AppleReceiptResponse {

    private int status;
    private AppleReceipt receipt;

    @Setter
    private boolean sandbox = false;
}
