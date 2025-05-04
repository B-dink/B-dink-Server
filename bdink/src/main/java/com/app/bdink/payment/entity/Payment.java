package com.app.bdink.payment.entity;

import com.app.bdink.payment.entity.embedded.CheckoutInfo;
import com.app.bdink.payment.entity.embedded.FailureInfo;
import com.app.bdink.payment.entity.embedded.ReceiptInfo;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 200, updatable = false)
    private String paymentKey; // 결제 키를 ID로 사용

    @Column(length = 20)
    private String version;

    @Column(length = 20)
    private String type; // NORMAL, BILLING, BRANDPAY 중 하나

    @Column(length = 64, updatable = false)
    private String orderId;

    @Column(length = 100)
    private String orderName;

    @Column(length = 14)
    private String mId;

    @Column(length = 10)
    private String currency;

    @Column(length = 30)
    private String method;

    @Column(precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 19, scale = 2)
    private BigDecimal balanceAmount;

    @Column(length = 20)
    private String status; // READY, IN_PROGRESS, WAITING_FOR_DEPOSIT, DONE, CANCELED, PARTIAL_CANCELED, ABORTED, EXPIRED

    private LocalDateTime requestedAt;

    private LocalDateTime approvedAt;

    @Column(columnDefinition = "boolean default false")
    private boolean useEscrow;

    @Column(length = 64)
    private String lastTransactionKey;

    @Column(precision = 19, scale = 2)
    private BigDecimal suppliedAmount;

    @Column(precision = 19, scale = 2)
    private BigDecimal vat;

    @Column(columnDefinition = "boolean default false")
    private boolean cultureExpense;

    @Column(precision = 19, scale = 2)
    private BigDecimal taxFreeAmount;

    @Column(precision = 19, scale = 0)
    private BigDecimal taxExemptionAmount;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentCancel> cancels = new ArrayList<>();

    @Column(columnDefinition = "boolean default false")
    private boolean isPartialCancelable;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private CardInfo cardInfo;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private VirtualAccount virtualAccount;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private MobilePhone mobilePhone;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private GiftCertificate giftCertificate;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Transfer transfer;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private EasyPay easyPay;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private CashReceipt cashReceipt;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CashReceiptHistory> cashReceipts = new ArrayList<>();

    @Column(length = 50)
    private String secret;

    @Column(length = 10)
    private String country;

    @Embedded
    private FailureInfo failure;

    @Embedded
    private ReceiptInfo receipt;

    @Embedded
    private CheckoutInfo checkout;

    // JSON 데이터를 저장하기 위한 필드
    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Builder
    public Payment(String paymentKey, String version, String type, String orderId, String orderName,
                   String mId, String currency, String method, BigDecimal totalAmount,
                   BigDecimal balanceAmount, String status, LocalDateTime requestedAt,
                   LocalDateTime approvedAt, boolean useEscrow, String lastTransactionKey,
                   BigDecimal suppliedAmount, BigDecimal vat, boolean cultureExpense,
                   BigDecimal taxFreeAmount, BigDecimal taxExemptionAmount, boolean isPartialCancelable,
                   String secret, String country) {
        this.paymentKey = paymentKey;
        this.version = version;
        this.type = type;
        this.orderId = orderId;
        this.orderName = orderName;
        this.mId = mId;
        this.currency = currency;
        this.method = method;
        this.totalAmount = totalAmount;
        this.balanceAmount = balanceAmount;
        this.status = status;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.useEscrow = useEscrow;
        this.lastTransactionKey = lastTransactionKey;
        this.suppliedAmount = suppliedAmount;
        this.vat = vat;
        this.cultureExpense = cultureExpense;
        this.taxFreeAmount = taxFreeAmount;
        this.taxExemptionAmount = taxExemptionAmount;
        this.isPartialCancelable = isPartialCancelable;
        this.secret = secret;
        this.country = country;
    }

    // 연관관계 편의 메서드
    public void addCancel(PaymentCancel cancel) {
        this.cancels.add(cancel);
        cancel.setPayment(this);
    }

    public void addCashReceiptHistory(CashReceiptHistory cashReceiptHistory) {
        this.cashReceipts.add(cashReceiptHistory);
        cashReceiptHistory.setPayment(this);
    }
}