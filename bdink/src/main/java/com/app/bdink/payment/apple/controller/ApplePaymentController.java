package com.app.bdink.payment.apple.controller;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.payment.apple.dto.AppleProductResponse;
import com.app.bdink.payment.apple.dto.PurchaseRequest;
import com.app.bdink.payment.apple.dto.PurchaseResponse;
import com.app.bdink.payment.apple.service.ApplePaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

//    @GetMapping("/products")
//    public RspTemplate<List<AppleProductResponse>> allProducts() {
//        return RspTemplate.success(Success.GET_APPLE_PRODUCTS_SUCCESS, applePaymentService.getAllProducts());
//    }

    @GetMapping("/product/{classRoomId}")
    public RspTemplate<AppleProductResponse> getProductByClassRoomId(
            @PathVariable Long classRoomId) {
        return RspTemplate.success(Success.GET_APPLE_PRODUCT_SUCCESS, applePaymentService.getProduct(classRoomId));
    }

    @GetMapping("/purchase-histories")
    public RspTemplate<?> purchaseHistory(Principal principal) {
        Long memberId = memberUtilService.getMemberId(principal);
        return RspTemplate.success(Success.GET_APPLE_PURCHASE_HISTORY, applePaymentService.getMemberPurchaseHistory(memberId));
    }
}
