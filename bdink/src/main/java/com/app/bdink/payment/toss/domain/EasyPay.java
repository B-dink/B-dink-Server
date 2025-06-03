package com.app.bdink.payment.toss.domain;

public record EasyPay(
        String provider,     // 간편결제사 코드
        Integer amount,       // 결제 금액
        Integer discountAmount // 즉시 할인 금액
) {
}
