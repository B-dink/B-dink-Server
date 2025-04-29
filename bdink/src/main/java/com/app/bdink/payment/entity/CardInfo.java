package com.app.bdink.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "card_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(length = 10)
    private String issuerCode;

    @Column(length = 10)
    private String acquirerCode;

    @Column(length = 20)
    private String number;

    private Integer installmentPlanMonths;

    @Column(length = 10)
    private String approveNo;

    @Column(columnDefinition = "boolean default false")
    private boolean useCardPoint;

    @Column(length = 20)
    private String cardType;

    @Column(length = 20)
    private String ownerType;

    @Column(length = 20)
    private String acquireStatus;

    @Column(columnDefinition = "boolean default false")
    private boolean isInterestFree;

    @Column(length = 20)
    private String interestPayer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_key")
    private Payment payment;

    @Builder
    public CardInfo(BigDecimal amount, String issuerCode, String acquirerCode, String number,
                    Integer installmentPlanMonths, String approveNo, boolean useCardPoint,
                    String cardType, String ownerType, String acquireStatus,
                    boolean isInterestFree, String interestPayer) {
        this.amount = amount;
        this.issuerCode = issuerCode;
        this.acquirerCode = acquirerCode;
        this.number = number;
        this.installmentPlanMonths = installmentPlanMonths;
        this.approveNo = approveNo;
        this.useCardPoint = useCardPoint;
        this.cardType = cardType;
        this.ownerType = ownerType;
        this.acquireStatus = acquireStatus;
        this.isInterestFree = isInterestFree;
        this.interestPayer = interestPayer;
    }
}