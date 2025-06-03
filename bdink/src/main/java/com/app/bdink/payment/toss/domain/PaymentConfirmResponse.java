package com.app.bdink.payment.toss.domain;

import java.util.List;

public record PaymentConfirmResponse(
        String version,
        String paymentKey,
        String type, //결제타입정보. NORMAL- 일반결제, BILLING-자동결제, BRANDPAY - 브랜드페이
        String orderId, //주문번호.결제 데이터 관리를 위해 반드시 저장.
        String orderName,
        String mId,
        String currency,
        String method,
        int totalAmount,
        int balanceAmount,
        String status,
        String requestedAt,
        String approvedAt,
        boolean useEscrow,
        String lastTransactionKey,  //한 결제의 승인거래와 취소거래 구분하는 키.
        int suppliedAmount,
        int vat, //(e.g. 결제 금액이 10,000원이고, 면세 금액이 3,000원이라면 부가세는 (10000-3000)/11 = 636.3636..을 반올림한 값 636원입니다.)
        boolean cultureExpense,
        int taxFreeAmount,
        int taxExemptionAmount, //과세를 제외한 결제금액. 이런애들은 부분취소안됨.
        List<Cancel> cancels,
        boolean isPartialCancelable,
        Card card,
        VirtualAccount virtualAccount,
        String secret, //. 가상계좌 웹훅 이벤트 본문으로 돌아온 secret과 같으면 정상적인 웹훅
        MobilePaymentInfo mobilePhone,  // 휴대폰 결제 정보 (nullable)
        BankTransfer transfer,          // 계좌이체 정보 (nullable)
        Receipt receipt,                // 영수증 정보 (nullable)
        Checkout checkout,              // 결제창 정보 (nullable)
        EasyPay easyPay, // 간편결제 정보 (nullable)
        String country,
        Failure failure,
        CashReceipt cashReceipt,
        List<CashReceiptHistory> cashReceipts,
        Discount discount
) {
}
