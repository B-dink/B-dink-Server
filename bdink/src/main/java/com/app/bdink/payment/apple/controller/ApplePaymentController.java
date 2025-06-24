package com.app.bdink.payment.apple.controller;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.payment.apple.dto.AppleProductResponse;
import com.app.bdink.payment.apple.dto.PurchaseRequest;
import com.app.bdink.payment.apple.dto.PurchaseResponse;
import com.app.bdink.payment.apple.service.ApplePaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment/apple")
@Tag(name = "Apple Payment", description = "애플 인앱 결제 API")
public class ApplePaymentController {

    private final ApplePaymentService applePaymentService;
    private final MemberUtilService memberUtilService;

    @PostMapping("/purchase")
    public RspTemplate<PurchaseResponse> purchase(
            Principal principal,
            @Valid @RequestBody PurchaseRequest purchaseRequest) {
        Long memberId = memberUtilService.getMemberId(principal);

        log.info("Apple purchase request initiated - memberId: {}, productId: {}",
                memberId, purchaseRequest.productId());

        try {
            PurchaseResponse purchaseResponse = applePaymentService.purchase(memberId, purchaseRequest);

            log.info("Apple purchase completed successfully - memberId: {}, productId: {}, transactionId: {}",
                    memberId, purchaseRequest.productId(), purchaseResponse.getTransactionId());

            return RspTemplate.success(Success.APPLE_PURCHASE_SUCCESS, purchaseResponse);
        } catch (Exception e) {
            log.error("Apple purchase failed in controller - memberId: {}, productId: {}, error: {}",
                    memberId, purchaseRequest.productId(), e.getMessage(), e);
            throw e;
        }
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
