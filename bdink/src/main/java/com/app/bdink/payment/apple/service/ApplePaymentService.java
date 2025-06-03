package com.app.bdink.payment.apple.service;

import com.app.bdink.payment.apple.dto.AppleProductResponse;
import com.app.bdink.payment.apple.dto.PurchaseRequest;
import com.app.bdink.payment.apple.dto.PurchaseResponse;
import com.app.bdink.payment.apple.entity.AppleProduct;

import java.util.List;

public interface ApplePaymentService {

    PurchaseResponse purchase(Long memberId, PurchaseRequest request);
    List<AppleProductResponse> getAllProducts();
}
