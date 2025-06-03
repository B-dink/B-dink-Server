package com.app.bdink.payment.toss.domain;

public record MobilePaymentInfo(
        String customerMobilePhone, // 결제한 휴대폰 번호
        String settlementStatus,    // 정산 상태 (INCOMPLETED, COMPLETED)
        String receiptUrl           // 결제 내역 영수증 URL
) {
}
