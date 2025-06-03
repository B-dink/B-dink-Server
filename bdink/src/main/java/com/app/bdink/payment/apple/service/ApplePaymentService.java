package com.app.bdink.payment.apple.service;

import com.app.bdink.payment.apple.dto.AppleProductResponse;
import com.app.bdink.payment.apple.dto.ApplePurchaseHistoryResponse;
import com.app.bdink.payment.apple.dto.PurchaseRequest;
import com.app.bdink.payment.apple.dto.PurchaseResponse;

import java.util.List;

public interface ApplePaymentService {

    List<AppleProductResponse> getAllProducts();
    List<ApplePurchaseHistoryResponse> getMemberPurchaseHistory(Long memberId);

    PurchaseResponse purchase(Long memberId, PurchaseRequest request);
}
