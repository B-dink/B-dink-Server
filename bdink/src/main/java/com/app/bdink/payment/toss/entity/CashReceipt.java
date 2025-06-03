package com.app.bdink.payment.toss.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "cash_receipt")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CashReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 20)
    private String type; // 소득공제, 지출증빙 중 하나

    @Column(length = 200)
    private String receiptKey;

    @Column(length = 9)
    private String issueNumber;

    @Column(length = 255)
    private String receiptUrl;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(precision = 19, scale = 2)
    private BigDecimal taxFreeAmount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Builder
    public CashReceipt(String type, String receiptKey, String issueNumber,
                       String receiptUrl, BigDecimal amount, BigDecimal taxFreeAmount) {
        this.type = type;
        this.receiptKey = receiptKey;
        this.issueNumber = issueNumber;
        this.receiptUrl = receiptUrl;
        this.amount = amount;
        this.taxFreeAmount = taxFreeAmount;
    }
}