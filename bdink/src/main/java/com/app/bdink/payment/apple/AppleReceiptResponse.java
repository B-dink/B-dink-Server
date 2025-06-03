package com.app.bdink.payment.apple;

import com.app.bdink.payment.domain.Receipt;
import lombok.Getter;

@Getter
public class AppleReceiptResponse {

    private int status;
    private AppleReceipt receipt;
}
