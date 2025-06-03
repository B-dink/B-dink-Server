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
        try {
            PurchaseResponse response = applePaymentService.purchase(memberId, purchaseRequest);

            if ("SUCCESS".equals(response.getStatus())) {
                return RspTemplate.success(Success.PURCHASE_SUCCESS, response);
            } else {
                return RspTemplate.fail(Error.PURCHASE_FAILED, response.getMessage());
            }

        } catch (IllegalArgumentException e) {
            return RspTemplate.fail(Error.INVALID_PURCHASE_REQUEST, e.getMessage());

        } catch (Exception e) {
            return RspTemplate.fail(Error.PURCHASE_PROCESSING_ERROR, "결제 처리 중 오류가 발생했습니다");
        }
    }

}
