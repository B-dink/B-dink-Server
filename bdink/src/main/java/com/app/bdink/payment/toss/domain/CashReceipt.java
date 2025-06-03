package com.app.bdink.payment.toss.domain;

public record CashReceipt(
        String type,              // 현금영수증 종류 (소득공제, 지출증빙)
        String receiptKey,        // 현금영수증 키값
        String issueNumber,       // 현금영수증 발급 번호
        String receiptUrl,        // 현금영수증 URL
        Integer amount,           // 현금영수증 처리된 금액
        Integer taxFreeAmount     // 면세 처리된 금액
) {}
