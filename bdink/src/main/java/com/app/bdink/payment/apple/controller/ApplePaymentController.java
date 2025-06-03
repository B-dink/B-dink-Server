package com.app.bdink.payment.apple.controller;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.payment.apple.dto.PurchaseRequest;
import com.app.bdink.payment.apple.dto.PurchaseResponse;
import com.app.bdink.payment.apple.service.ApplePaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment/apple")
public class ApplePaymentController {

    private final ApplePaymentService applePaymentService;
    private final MemberUtilService memberUtilService;

    @PostMapping("/purchase")
    public RspTemplate<PurchaseResponse> purchase(
            Principal principal,
            @Valid @RequestBody PurchaseRequest purchaseRequest) {
        Long memberId = memberUtilService.getMemberId(principal);
        PurchaseResponse purchaseResponse = applePaymentService.purchase(memberId, purchaseRequest);
        return RspTemplate.success(Success.APPLE_PURCHASE_SUCCESS, purchaseResponse);
    }
}
