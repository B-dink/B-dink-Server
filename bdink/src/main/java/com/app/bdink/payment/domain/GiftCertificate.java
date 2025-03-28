package com.app.bdink.payment.domain;

public record GiftCertificate(
        String approveNo,       // 결제 승인 번호
        String settlementStatus // 정산 상태
) {}