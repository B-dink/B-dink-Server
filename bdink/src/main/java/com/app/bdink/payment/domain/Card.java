package com.app.bdink.payment.domain;

public record Card(
        Integer amount,                // 카드사에 결제 요청한 금액
        String issuerCode,            // 카드 발급사 코드
        String acquirerCode,          // 카드 매입사 코드 (nullable)
        String number,                // 카드 번호 (마스킹 처리됨)
        Integer installmentPlanMonths,// 할부 개월 수 (0이면 일시불)
        String approveNo,             // 카드 승인 번호
        Boolean useCardPoint,         // 카드 포인트 사용 여부
        String cardType,              // 카드 종류 (신용, 체크, 기프트 등)
        String ownerType,             // 카드 소유자 타입 (개인, 법인 등)
        String acquireStatus,         // 카드 매입 상태 (READY, COMPLETED 등)
        Boolean isInterestFree,       // 무이자 할부 적용 여부
        String interestPayer          // 할부 수수료 부담 주체 (BUYER, CARD_COMPANY, MERCHANT 등)
) {
}
