package com.app.bdink.payment.controller.dto;

public record ConfirmResponse(
        String mId,
        String lastTransactionKey,
        String paymentKey,
        String orderId,
        String orderName,
        int taxExemptionAmount,
        String status,
        String requestedAt,
        String approvedAt,
        boolean useEscrow,
        boolean cultureExpense,
        CardInfo card,
        Object virtualAccount,
        Object transfer,
        Object mobilePhone,
        Object giftCertificate,
        Object cashReceipt,
        Object cashReceipts,
        Object discount,
        Object cancels,
        Object secret,
        String type,
        EasyPayInfo easyPay,
        String country,
        Object failure,
        boolean isPartialCancelable,
        ReceiptInfo receipt,
        CheckoutInfo checkout,
        String currency,
        int totalAmount,
        int balanceAmount,
        int suppliedAmount,
        int vat,
        int taxFreeAmount,
        Object metadata,
        String method,
        String version
) {
    public record CardInfo(
            String issuerCode,
            String acquirerCode,
            String number,
            int installmentPlanMonths,
            boolean isInterestFree,
            String interestPayer,
            String approveNo,
            boolean useCardPoint,
            String cardType,
            String ownerType,
            String acquireStatus,
            int amount
    ) {}

    public record EasyPayInfo(
            String provider,
            int amount,
            int discountAmount
    ) {}

    public record ReceiptInfo(
            String url
    ) {}

    public record CheckoutInfo(
            String url
    ) {}
}