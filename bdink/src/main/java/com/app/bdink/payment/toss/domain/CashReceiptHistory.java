package com.app.bdink.payment.toss.domain;

public record CashReceiptHistory(
        String receiptKey,          // 현금영수증 키값
        String orderId,             // 주문번호
        String orderName,           // 구매상품 정보
        String type,                // 현금영수증 종류 (소득공제, 지출증빙)
        String issueNumber,         // 현금영수증 발급 번호
        String receiptUrl,          // 현금영수증 URL
        String businessNumber,      // 발급한 사업자등록번호
        String transactionType,     // 현금영수증 발급 종류 (CONFIRM, CANCEL)
        Integer amount,             // 현금영수증 처리된 금액
        Integer taxFreeAmount,      // 면세 처리된 금액
        String issueStatus,         // 발급 상태 (IN_PROGRESS, COMPLETED, FAILED)
        Failure failure,            // 결제 실패 객체
        String customerIdentityNumber, // 소비자 인증수단
        String requestedAt          // 발급 혹은 취소 요청 시간 (ISO 8601)
) {}

