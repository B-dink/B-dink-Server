package com.app.bdink.payment.toss.domain;

public record BankTransfer(
        String bankCode,        // 은행 코드
        String settlementStatus // 정산 상태
) {}
