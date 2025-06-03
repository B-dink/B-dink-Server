package com.app.bdink.payment.apple.service;

import com.app.bdink.payment.apple.dto.PurchaseRequest;
import com.app.bdink.payment.apple.dto.PurchaseResponse;

public interface ApplePaymentService {

    PurchaseResponse purchase(Long memberId, PurchaseRequest request);
}
