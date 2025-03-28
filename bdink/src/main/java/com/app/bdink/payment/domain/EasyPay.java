package com.app.bdink.payment.domain;

public record EasyPay(
        String provider,     // 간편결제사 코드
        Double amount,       // 결제 금액
        Double discountAmount // 즉시 할인 금액
) {
}
