package com.app.bdink.payment.domain;

public record VirtualAccount(
        String accountType,        // 가상계좌 타입 (일반, 고정)
        String accountNumber,      // 발급된 계좌번호
        String bankCode,           // 가상계좌 은행 코드
        String customerName,       // 구매자명
        String dueDate,            // 입금 기한 (ISO 8601 형식)
        String refundStatus,       // 환불 상태 (NONE, PENDING, COMPLETED 등)
        Boolean expired,           // 가상계좌 만료 여부
        String settlementStatus,   // 정산 상태 (INCOMPLETED, COMPLETED)
        RefundReceiveAccount refundReceiveAccount // 환불 계좌 정보 (nullable)
) {

}
