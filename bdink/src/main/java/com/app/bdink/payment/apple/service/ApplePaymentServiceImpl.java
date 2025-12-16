package com.app.bdink.payment.apple.service;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.global.exception.model.PaymentFailedException;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.payment.apple.entity.ApplePurchaseHistory;
import com.app.bdink.payment.apple.repository.AppleProductRepository;
import com.app.bdink.payment.apple.dto.*;
import com.app.bdink.payment.apple.entity.AppleProduct;
import com.app.bdink.payment.apple.repository.ApplePurchaseHistoryRepository;
import com.app.bdink.payment.transactional.PaymentTransactionalService;
import com.app.bdink.sugang.controller.dto.SugangStatus;
import com.app.bdink.sugang.service.SugangService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.client.RestTemplate;
import com.app.bdink.global.exception.Error;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ApplePaymentServiceImpl implements ApplePaymentService {

    private static final String PRODUCTION_URL = "https://buy.itunes.apple.com/verifyReceipt";
    private static final String SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";

    private final AppleProductRepository appleProductRepository;
    private final ApplePurchaseHistoryRepository applePurchaseHistoryRepository;
    private final RestTemplate restTemplate;

    private final SugangService sugangService;
    private final MemberService memberService;

    // 결제 취소에 대한 처리 필요
    private final PaymentTransactionalService paymentTransactionalService;

    @Override
    public AppleProductResponse getProduct(Long classRoomId) {
        return appleProductRepository.findByClassRoomId(classRoomId)
                .map(AppleProductResponse::from)
                .orElseThrow(NoSuchElementException::new);
    }

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
        log.info("Starting secure purchase process - memberId: {}, productId: {}",
                memberId, request.productId());
        return executeSecurePurchase(memberId, request);
    }

    private PurchaseResponse executeSecurePurchase(Long memberId, PurchaseRequest request) {
        log.debug("Executing secure purchase - memberId: {}, productId: {}",
                memberId, request.productId());

        long startTime = System.currentTimeMillis();

        try {
            // 1. 락을 걸고 상품 조회 및 검증
            log.debug("Step 1: Acquiring product lock - memberId: {}, productId: {}",
                    memberId, request.productId());
            AppleProduct product = acquireProductLock(memberId, request.productId());
            log.info("Product lock acquired successfully - memberId: {}, productId: {}, canPurchase: {}",
                    memberId, request.productId(), product.getCanPurchase());

            // 2. Apple 영수증 검증
            log.debug("Step 2: Verifying Apple receipt - memberId: {}, productId: {}",
                    memberId, request.productId());
            InAppPurchase inAppPurchase = verifyAppleReceipt(request);
            log.info("Apple receipt verified successfully - memberId: {}, productId: {}, transactionId: {}, originalTransactionId: {}",
                    memberId, request.productId(), inAppPurchase.getTransactionId(), inAppPurchase.getOriginalTransactionId());

            // 3. 구매 이력 저장
            log.debug("Step 3: Saving purchase history - memberId: {}, productId: {}, transactionId: {}",
                    memberId, request.productId(), inAppPurchase.getTransactionId());
            savePurchaseHistoryWithDuplicateCheck(memberId, inAppPurchase);
            log.info("Purchase history saved successfully - memberId: {}, productId: {}, transactionId: {}",
                    memberId, request.productId(), inAppPurchase.getTransactionId());

            // 4. 결제 성공 후 자동으로 수강 시작
            log.debug("Step 4: Starting sugang after successful payment - memberId: {}, productId: {}",
                    memberId, request.productId());
            startSugangAfterPayment(memberId, product.getClassRoom());
            log.info("Sugang started successfully after payment - memberId: {}, classRoomId: {}",
                    memberId, product.getClassRoom().getId());

            long processingTime = System.currentTimeMillis() - startTime;
            log.info("Purchase and sugang process completed successfully - memberId: {}, productId: {}, transactionId: {}, processingTime: {}ms",
                    memberId, request.productId(), inAppPurchase.getTransactionId(), processingTime);

            return createSuccessResponse(inAppPurchase.getTransactionId());
        } catch (Exception e) {
            long processingTime = System.currentTimeMillis() - startTime;
            log.error("Purchase failed - memberId: {}, productId: {}, processingTime: {}ms, error: {}",
                    memberId, request.productId(), processingTime, e.getMessage(), e);
            throw e;
        }
    }

    private void startSugangAfterPayment(Long memberId, ClassRoomEntity classRoom) {
        try {
            log.debug("Starting sugang after payment - memberId: {}, classRoomId: {}",
                    memberId, classRoom.getId());

            Member member = memberService.findById(memberId);
            sugangService.createSugang(classRoom, member, SugangStatus.PAYMENT_COMPLETED);
        } catch (Exception e) {
            log.error("Failed to start sugang after payment - memberId: {}, classRoomId: {}, error: {}",
                    memberId, classRoom.getId(), e.getMessage(), e);
            // 결제는 성공했으므로 수강 시작 실패 시에도 결제 성공으로 처리
            // 대신 관리자에게 알림이나 별도 처리 필요할 수 있음
            throw new PaymentFailedException(Error.SUGANG_STATUS_UPDATE, Error.SUGANG_STATUS_UPDATE.getMessage());
        }
    }

    private AppleProduct acquireProductLock(Long memberId, String productId) {
        log.debug("Acquiring product lock - memberId: {}, productId: {}", memberId, productId);

        try {
            // 1. 애플 상품 락으로 체크
            log.debug("Checking product availability with lock - productId: {}", productId);
            AppleProduct product = appleProductRepository.findByProductIdWithLock(productId)
                    .orElseThrow(() -> {
                        log.warn("Product not found - productId: {}", productId);
                        return new PaymentFailedException(Error.PRODUCT_NOT_FOUND,
                                "Product not found: " + productId);
                    });

            log.debug("Product found - productId: {}, canPurchase: {}", productId, product.getCanPurchase());

            if (!product.getCanPurchase()) {
                log.warn("Product not available for purchase - productId: {}", productId);
                throw new PaymentFailedException(Error.PRODUCT_NOT_AVAILABLE,
                        Error.PRODUCT_NOT_AVAILABLE.getMessage() + productId);
            }

            // 2. 구매 이력도 락으로 체크
            log.debug("Checking existing purchases with lock - memberId: {}, productId: {}", memberId, productId);
            boolean existingPurchases = applePurchaseHistoryRepository.findByUserIdAndProductIdWithLock(memberId, productId);

            if (existingPurchases) {
                log.warn("Duplicate purchase attempt detected - memberId: {}, productId: {}", memberId, productId);
                throw new PaymentFailedException(Error.DUPLICATE_PURCHASE,
                        Error.DUPLICATE_PURCHASE.getMessage() + productId);
            }

            log.debug("No existing purchases found - memberId: {}, productId: {}", memberId, productId);
            return product;

        } catch (PessimisticLockingFailureException e) {
            log.warn("Concurrent purchase attempt detected - memberId: {}, productId: {}, error: {}",
                    memberId, productId, e.getMessage());
            throw new PaymentFailedException(Error.CONCURRENT_PURCHASE_ATTEMPT,
                    Error.CONCURRENT_PURCHASE_ATTEMPT.getMessage());
        }
    }

    private InAppPurchase verifyAppleReceipt(PurchaseRequest request) {
        log.debug("Starting Apple receipt verification - productId: {}", request.productId());

        try {
            AppleReceiptResponse appleReceiptResponse = verifyPurchase(request.appStoreReceipt());
            InAppPurchase inAppPurchase = getInAppPurchase(request, appleReceiptResponse);

            log.info("Apple receipt verification completed - productId: {}, transactionId: {}, originalTransactionId: {}",
                    request.productId(), inAppPurchase.getTransactionId(), inAppPurchase.getOriginalTransactionId());

            return inAppPurchase;
        } catch (PaymentFailedException e) {
            log.error("Apple receipt verification failed - productId: {}, error: {}",
                    request.productId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during Apple receipt verification - productId: {}, error: {}",
                    request.productId(), e.getMessage(), e);
            throw new PaymentFailedException(Error.APPLE_VERIFICATION_FAILED,
                    "Apple receipt verification failed: " + e.getMessage());
        }
    }

    private AppleReceiptResponse verifyPurchase(String receiptData) {
        log.debug("Verifying purchase with Apple - receiptData length: {}", receiptData.length());

        Map<String, String> payload = Map.of("receipt-data", receiptData);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            log.debug("Attempting Apple production verification");
            ResponseEntity<AppleReceiptResponse> response = restTemplate.exchange(
                    PRODUCTION_URL,
                    HttpMethod.POST,
                    new HttpEntity<>(payload, headers),
                    AppleReceiptResponse.class
            );

            AppleReceiptResponse receiptResponse = Optional.ofNullable(response.getBody())
                    .orElseThrow(() -> {
                        log.error("Apple production API returned null response");
                        return new PaymentFailedException(Error.APPLE_VERIFICATION_FAILED,
                                "Apple API returned null response");
                    });

            log.debug("Apple production response received - status: {}", receiptResponse.getStatus());

            if (receiptResponse.getStatus() == 21007) {
                log.info("Production returned 21007, retrying with sandbox");

                receiptResponse.setSandbox(true);

                response = restTemplate.exchange(
                        SANDBOX_URL,
                        HttpMethod.POST,
                        new HttpEntity<>(payload, headers),
                        AppleReceiptResponse.class
                );

                receiptResponse = Optional.ofNullable(response.getBody())
                        .orElseThrow(() -> {
                            log.error("Apple sandbox API returned null response");
                            return new PaymentFailedException(Error.APPLE_VERIFICATION_FAILED,
                                    "Apple Sandbox API returned null response");
                        });

                log.debug("Apple sandbox response received - status: {}", receiptResponse.getStatus());

                if (receiptResponse.getStatus() != 0) {
                    log.error("Sandbox verification failed - status: {}", receiptResponse.getStatus());
                    throw new PaymentFailedException(Error.APPLE_VERIFICATION_FAILED,
                            "Sandbox verification also failed with status: " + receiptResponse.getStatus());
                }

                log.info("Apple sandbox verification successful");
            } else if (receiptResponse.getStatus() != 0) {
                log.error("Apple production verification failed - status: {}", receiptResponse.getStatus());
                throw new PaymentFailedException(Error.APPLE_VERIFICATION_FAILED,
                        "Apple verification failed with status: " + receiptResponse.getStatus());
            } else {
                log.info("Apple production verification successful");
            }

            return receiptResponse;
        } catch (PaymentFailedException e) {
            throw e;
        } catch (Exception e) {
            log.error("Network error during Apple verification - error: {}", e.getMessage(), e);
            throw new PaymentFailedException(Error.APPLE_VERIFICATION_FAILED,
                    "Network error during Apple verification: " + e.getMessage());
        }
    }

    private InAppPurchase getInAppPurchase(PurchaseRequest request, AppleReceiptResponse appleReceiptResponse) {
        log.debug("Extracting in-app purchase data from receipt - requestedProductId: {}", request.productId());

        AppleReceipt receipt = appleReceiptResponse.getReceipt();

        if (receipt.getInApp() == null || receipt.getInApp().isEmpty()) {
            log.error("No in-app purchase data found in receipt - requestedProductId: {}", request.productId());
            throw new PaymentFailedException(Error.INVALID_RECEIPT, "No in-app purchase data found in receipt");
        }

        InAppPurchase inAppPurchase = receipt.getInApp().get(0);
        log.debug("In-app purchase data extracted - receiptProductId: {}, transactionId: {}",
                inAppPurchase.getProductId(), inAppPurchase.getTransactionId());

        if (!request.productId().equals(inAppPurchase.getProductId())) {
            if (appleReceiptResponse.isSandbox()) {
                log.warn("Product mismatch in sandbox environment (bypassing) - requested: {}, receipt: {}",
                        request.productId(), inAppPurchase.getProductId());
            } else {
                log.error("Product mismatch - requested: {}, receipt: {}",
                        request.productId(), inAppPurchase.getProductId());
                throw new PaymentFailedException(Error.PRODUCT_MISMATCH,
                        String.format("Product mismatch - requested: %s, receipt: %s",
                                request.productId(), inAppPurchase.getProductId()));
            }
        }

        log.debug("Product verification successful - productId: {}", request.productId());
        return inAppPurchase;
    }

    private void savePurchaseHistoryWithDuplicateCheck(Long memberId, InAppPurchase inAppPurchase) {
        log.debug("Saving purchase history - memberId: {}, productId: {}, transactionId: {}, originalTransactionId: {}",
                memberId, inAppPurchase.getProductId(), inAppPurchase.getTransactionId(), inAppPurchase.getOriginalTransactionId());

        try {
            ApplePurchaseHistory applePurchaseHistory = ApplePurchaseHistory.createPurchase(
                    memberId,
                    inAppPurchase.getProductId(),
                    inAppPurchase.getTransactionId(),
                    inAppPurchase.getOriginalTransactionId());

            applePurchaseHistoryRepository.save(applePurchaseHistory);
            log.info("Purchase history saved successfully - memberId: {}, productId: {}, transactionId: {}",
                    memberId, inAppPurchase.getProductId(), inAppPurchase.getTransactionId());

        } catch (DataIntegrityViolationException e) {
            log.warn("Data integrity violation detected, handling duplicate transaction - memberId: {}, productId: {}, transactionId: {}, error: {}",
                    memberId, inAppPurchase.getProductId(), inAppPurchase.getTransactionId(), e.getMessage());

            handleDuplicateTransaction(memberId, inAppPurchase);
        }
    }

    private void handleDuplicateTransaction(Long memberId, InAppPurchase inAppPurchase) {
        log.info("Handling duplicate transaction - memberId: {}, productId: {}, transactionId: {}, originalTransactionId: {}",
                memberId, inAppPurchase.getProductId(), inAppPurchase.getTransactionId(), inAppPurchase.getOriginalTransactionId());

        ApplePurchaseHistory existing = applePurchaseHistoryRepository
                .findByUserIdAndOriginalTransactionIdAndProductId(
                        memberId, inAppPurchase.getOriginalTransactionId(), inAppPurchase.getProductId())
                .orElseThrow(() -> {
                    log.error("Constraint violation but no existing record found - memberId: {}, originalTransactionId: {}, productId: {}",
                            memberId, inAppPurchase.getOriginalTransactionId(), inAppPurchase.getProductId());
                    return new PaymentFailedException(Error.INTERNAL_SERVER_ERROR,
                            "Constraint violation but no existing record found");
                });

        log.debug("Found existing purchase - memberId: {}, existingTransactionId: {}, newTransactionId: {}",
                memberId, existing.getTransactionId(), inAppPurchase.getTransactionId());

        // 중복 결제 상황
        if (existing.getTransactionId().equals(inAppPurchase.getTransactionId())) {
            log.warn("Duplicate purchase detected - memberId: {}, transactionId: {}",
                    memberId, inAppPurchase.getTransactionId());
            throw new PaymentFailedException(Error.DUPLICATE_PURCHASE,
                    "This purchase has already been processed: " + inAppPurchase.getTransactionId());
        }

        // 복원 상황
        log.info("Purchase restoration detected - memberId: {}, productId: {}, originalTransactionId: {}, existingTransactionId: {}, newTransactionId: {}",
                memberId, inAppPurchase.getProductId(), inAppPurchase.getOriginalTransactionId(),
                existing.getTransactionId(), inAppPurchase.getTransactionId());
    }

    private PurchaseResponse createSuccessResponse(String transactionId) {
        log.debug("Creating success response - transactionId: {}", transactionId);

        return PurchaseResponse.builder()
                .status("SUCCESS")
                .message("Purchase verified successfully")
                .transactionId(transactionId)
                .build();
    }
}
