package com.app.bdink.payment.domain;

public record PaymentInfo(
        MobilePaymentInfo mobilePhone,  // 휴대폰 결제 정보 (nullable)
        GiftCertificate giftCertificate, // 상품권 결제 정보 (nullable)
        BankTransfer transfer,          // 계좌이체 정보 (nullable)
        Receipt receipt,                // 영수증 정보 (nullable)
        Checkout checkout,              // 결제창 정보 (nullable)
        EasyPay easyPay                 // 간편결제 정보 (nullable)
) {}
