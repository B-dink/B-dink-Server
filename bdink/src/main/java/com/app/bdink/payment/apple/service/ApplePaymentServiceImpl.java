package com.app.bdink.payment.apple.service;

import com.app.bdink.global.exception.model.PaymentFailedException;
import com.app.bdink.payment.apple.entity.ApplePurchaseHistory;
import com.app.bdink.payment.apple.repository.AppleProductRepository;
import com.app.bdink.payment.apple.dto.*;
import com.app.bdink.payment.apple.entity.AppleProduct;
import com.app.bdink.payment.apple.repository.ApplePurchaseHistoryRepository;
import com.app.bdink.payment.transactional.PaymentTransactionalService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.client.RestTemplate;
import com.app.bdink.global.exception.Error;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplePaymentServiceImpl implements ApplePaymentService {

    private static final String PRODUCTION_URL = "https://buy.itunes.apple.com/verifyReceipt";
    private static final String SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";

    private final AppleProductRepository appleProductRepository;
    private final ApplePurchaseHistoryRepository applePurchaseHistoryRepository;
    private final RestTemplate restTemplate;

    // 결제 취소에 대한 처리 필요
    private final PaymentTransactionalService paymentTransactionalService;

    @Override
    public List<AppleProductResponse> getAllProducts() {
        return appleProductRepository.findAll()
                .stream()
                .map(AppleProductResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplePurchaseHistoryResponse> getMemberPurchaseHistory(Long memberId) {
        return applePurchaseHistoryRepository.findAllByUserId(memberId)
                .stream()
                .map(ApplePurchaseHistoryResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseResponse purchase(Long memberId, PurchaseRequest request) {
        validateProduct(request.productId());
        InAppPurchase inAppPurchase = verifyAppleReceipt(request);
        savePurchaseHistoryWithDuplicateCheck(memberId, inAppPurchase);
        return createSuccessResponse(inAppPurchase.getTransactionId());
    }

    private void validateProduct(String productId) {
        AppleProduct product = appleProductRepository.findByProductId(productId)
                .orElseThrow(() -> new PaymentFailedException(Error.PRODUCT_NOT_FOUND, "Product not found: " + productId));

        if (!product.getCanPurchase()) {
            throw new PaymentFailedException(Error.PRODUCT_NOT_AVAILABLE,
                    "Product not available for purchase: " + productId);
        }
    }

    private InAppPurchase verifyAppleReceipt(PurchaseRequest request) {
        try {
            AppleReceiptResponse appleReceiptResponse = verifyPurchase(request.appStoreReceipt());
            return getInAppPurchase(request, appleReceiptResponse);
        } catch (PaymentFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new PaymentFailedException(Error.APPLE_VERIFICATION_FAILED,
                    "Apple receipt verification failed: " + e.getMessage());
        }
    }

    private AppleReceiptResponse verifyPurchase(String receiptData) {
        Map<String, String> payload = Map.of("receipt-data", receiptData);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<AppleReceiptResponse> response = restTemplate.exchange(
                    PRODUCTION_URL,
                    HttpMethod.POST,
                    new HttpEntity<>(payload, headers),
                    AppleReceiptResponse.class
            );

            AppleReceiptResponse receiptResponse = Optional.ofNullable(response.getBody())
                    .orElseThrow(() -> new PaymentFailedException(Error.APPLE_VERIFICATION_FAILED,
                            "Apple API returned null response"));

            // 3. 상태코드 확인 - 21007의 경우 테스트 영수증임
            if (receiptResponse.getStatus() == 21007) {
                // Sandbox 환경으로 재시도
                response = restTemplate.exchange(
                        SANDBOX_URL,
                        HttpMethod.POST,
                        new HttpEntity<>(payload, headers),
                        AppleReceiptResponse.class
                );
                receiptResponse = Optional.ofNullable(response.getBody())
                        .orElseThrow(() -> new PaymentFailedException(Error.APPLE_VERIFICATION_FAILED,
                                "Apple Sandbox API returned null response"));
            }

            if (receiptResponse.getStatus() != 0) {
                // RuntimeException → PaymentFailedException
                throw new PaymentFailedException(Error.APPLE_VERIFICATION_FAILED,
                        "Apple verification failed with status: " + receiptResponse.getStatus());
            }

            return receiptResponse;
        } catch (PaymentFailedException e) {
            throw e;
        } catch (Exception e) {
            // RuntimeException → PaymentFailedException
            throw new PaymentFailedException(Error.APPLE_VERIFICATION_FAILED,
                    "Network error during Apple verification: " + e.getMessage());
        }
    }

    @NotNull
    private static InAppPurchase getInAppPurchase(PurchaseRequest request, AppleReceiptResponse appleReceiptResponse) {
        AppleReceipt receipt = appleReceiptResponse.getReceipt();

        if (receipt.getInApp() == null || receipt.getInApp().isEmpty()) {
            throw new PaymentFailedException(Error.INVALID_RECEIPT, "No in-app purchase data found in receipt");
        }

        InAppPurchase inAppPurchase = receipt.getInApp().get(0);

        if (!request.productId().equals(inAppPurchase.getProductId())) {
            throw new PaymentFailedException(Error.PRODUCT_MISMATCH,
                    String.format("Product mismatch - requested: %s, receipt: %s",
                            request.productId(), inAppPurchase.getProductId()));
        }
        return inAppPurchase;
    }

    private void savePurchaseHistoryWithDuplicateCheck(Long memberId, InAppPurchase inAppPurchase) {
        try {
            ApplePurchaseHistory applePurchaseHistory = ApplePurchaseHistory.createPurchase(
                    memberId,
                    inAppPurchase.getProductId(),
                    inAppPurchase.getTransactionId(),
                    inAppPurchase.getOriginalTransactionId());

            applePurchaseHistoryRepository.save(applePurchaseHistory);
        } catch (DataIntegrityViolationException e) {
            // 유니크 제약조건 위반 = 이미 존재, 복원인지 중복인지 확인해서 적절히 처리
            handleDuplicateTransaction(memberId, inAppPurchase);
        }
    }

    private void handleDuplicateTransaction(Long memberId, InAppPurchase inAppPurchase) {
        ApplePurchaseHistory existing = applePurchaseHistoryRepository
                .findByUserIdAndOriginalTransactionIdAndProductId(
                        memberId, inAppPurchase.getOriginalTransactionId(), inAppPurchase.getProductId())
                .orElseThrow(() -> new PaymentFailedException(Error.INTERNAL_SERVER_ERROR,
                        "Constraint violation but no existing record found"));

        // 중복 결제 상황
        if (existing.getTransactionId().equals(inAppPurchase.getTransactionId())) {
            throw new PaymentFailedException(Error.DUPLICATE_PURCHASE,
                    "This purchase has already been processed: " + inAppPurchase.getTransactionId());
        }

        // 복원, DB 업데이트 없이 그냥 성공 처리
        return;
    }

    private PurchaseResponse createSuccessResponse(String transactionId) {
        return PurchaseResponse.builder()
                .status("SUCCESS")
                .message("Purchase verified successfully")
                .transactionId(transactionId)
                .build();
    }
}
