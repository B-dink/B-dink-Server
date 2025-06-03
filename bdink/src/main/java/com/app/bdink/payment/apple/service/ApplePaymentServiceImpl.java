package com.app.bdink.payment.apple.service;

import com.app.bdink.payment.apple.entity.ApplePurchaseHistory;
import com.app.bdink.payment.apple.repository.AppleProductRepository;
import com.app.bdink.payment.apple.dto.*;
import com.app.bdink.payment.apple.entity.AppleProduct;
import com.app.bdink.payment.apple.repository.ApplePurchaseHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplePaymentServiceImpl implements ApplePaymentService {

    private static final String PRODUCTION_URL = "https://buy.itunes.apple.com/verifyReceipt";
    private static final String SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";

    private final AppleProductRepository appleProductRepository;
    private final ApplePurchaseHistoryRepository applePurchaseHistoryRepository;

    @Override
    public PurchaseResponse purchase(Long memberId, PurchaseRequest request) {
        try {
            ValidationResult validation = validatePurchaseRequest(request);
            if (!validation.isValid()) {
                return createFailResponse(validation.getErrorMessage());
            }

            // 중복 구매 체크
            if (isDuplicatePurchase(validation.getTransactionId())) {
                return createSuccessResponse(validation.getTransactionId(), "Already processed");
            }

            // DB에 구매 정보 저장
            savePurchaseHistory(memberId, validation.getInAppPurchase());

            return createSuccessResponse(validation.getTransactionId(), "Purchase verified successfully");
        } catch (Exception e) {
            return createFailResponse("Purchase verification failed: " + e.getMessage());
        }
    }

    private ValidationResult validatePurchaseRequest(PurchaseRequest request) {
        // 상품 존재 체크
        if (!isProductExistAndCanPurchase(request.productId())) {
            return ValidationResult.fail("Product does not exist");
        }

        // Apple 검증
        AppleReceiptResponse appleReceiptResponse = verifyPurchase(request.appStoreReceipt());
        AppleReceipt receipt = appleReceiptResponse.getReceipt();

        if (receipt.getInApp() == null || receipt.getInApp().isEmpty()) {
            return ValidationResult.fail("Apple receipt is empty");
        }

        InAppPurchase inAppPurchase = receipt.getInApp().get(0);

        if (!request.productId().equals(inAppPurchase.getProductId())) {
            return ValidationResult.fail("Product ID mismatch");
        }

        return ValidationResult.success(inAppPurchase);
    }

    private Boolean isProductExistAndCanPurchase(String productId) {
        return appleProductRepository.findByProductId(productId)
                .map(AppleProduct::getCanPurchase)
                .orElse(false);
    }

    private AppleReceiptResponse verifyPurchase(String receiptData) {
        RestTemplate restTemplate = new RestTemplate();

        // 1. Apple에 보낼 payload 구성
        Map<String, String> payload = new HashMap<>();
        payload.put("receipt-data", receiptData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            // 2. Production URL 먼저 시도
            ResponseEntity<AppleReceiptResponse> response = restTemplate.exchange(
                    PRODUCTION_URL,
                    HttpMethod.POST,
                    new HttpEntity<>(payload, headers),
                    AppleReceiptResponse.class
            );

            AppleReceiptResponse receiptResponse = response.getBody();

            // 3. 상태코드 확인 - 21007의 경우 테스트 영수증임
            if (Objects.requireNonNull(receiptResponse).getStatus() == 21007) {
                // Sandbox 환경으로 재시도
                response = restTemplate.exchange(
                        SANDBOX_URL,
                        HttpMethod.POST,
                        new HttpEntity<>(payload, headers),
                        AppleReceiptResponse.class
                );
                return response.getBody();
            } else if (receiptResponse.getStatus() != 0) {
                throw new RuntimeException("Apple receipt verification failed: " + receiptResponse.getStatus());
            }

            return receiptResponse;

        } catch (Exception e) {
            throw new RuntimeException("Receipt verification failed", e);
        }
    }

    private boolean isDuplicatePurchase(String transactionId) {
        return applePurchaseHistoryRepository.existsByTransactionId(transactionId);
    }

    private void savePurchaseHistory(Long memberId, InAppPurchase inAppPurchase) {
        ApplePurchaseHistory applePurchaseHistory = ApplePurchaseHistory.createPurchase(
                memberId, inAppPurchase.getProductId(), inAppPurchase.getTransactionId());
        applePurchaseHistoryRepository.save(applePurchaseHistory);
    }

    private PurchaseResponse createSuccessResponse(String transactionId, String message) {
        return PurchaseResponse.builder()
                .status("SUCCESS")
                .message(message)
                .transactionId(transactionId)
                .build();
    }

    private PurchaseResponse createFailResponse(String message) {
        return PurchaseResponse.builder()
                .status("FAIL")
                .message(message)
                .build();
    }
}
