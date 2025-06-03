package com.app.bdink.payment.toss.domain;

public record RefundReceiveAccount(
        String bankCode,        // 은행 코드
        String accountNumber,   // 계좌 번호
        String holderName       // 예금주 이름
) {
}
