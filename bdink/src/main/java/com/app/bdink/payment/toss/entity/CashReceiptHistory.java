package com.app.bdink.payment.toss.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "cash_receipt_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CashReceiptHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 200)
    private String receiptKey;

    @Column(length = 64)
    private String orderId;

    @Column(length = 100)
    private String orderName;

    @Column(length = 20)
    private String type; // 소득공제, 지출증빙 중 하나

    @Column(length = 9)
    private String issueNumber;

    @Column(length = 255)
    private String receiptUrl;

    @Column(length = 10)
    private String businessNumber;

    @Column(length = 20)
    private String transactionType; // CONFIRM, CANCEL

    private Integer amount;

    private Integer taxFreeAmount;

    @Column(length = 20)
    private String issueStatus; // IN_PROGRESS, COMPLETED, FAILED

    @Column(columnDefinition = "TEXT")
    private String failure; // JSON으로 저장

    @Column(length = 30)
    private String customerIdentityNumber;

    private LocalDateTime requestedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Builder
    public CashReceiptHistory(String receiptKey, String orderId, String orderName, String type,
                              String issueNumber, String receiptUrl, String businessNumber,
                              String transactionType, Integer amount, Integer taxFreeAmount,
                              String issueStatus, String failure, String customerIdentityNumber,
                              LocalDateTime requestedAt) {
        this.receiptKey = receiptKey;
        this.orderId = orderId;
        this.orderName = orderName;
        this.type = type;
        this.issueNumber = issueNumber;
        this.receiptUrl = receiptUrl;
        this.businessNumber = businessNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.taxFreeAmount = taxFreeAmount;
        this.issueStatus = issueStatus;
        this.failure = failure;
        this.customerIdentityNumber = customerIdentityNumber;
        this.requestedAt = requestedAt;
    }
}