package com.app.bdink.payment.toss.entity;

import com.app.bdink.payment.toss.entity.embedded.RefundReceiveAccount;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "virtual_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VirtualAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 20)
    private String accountType; // 일반, 고정 중 하나

    @Column(length = 20)
    private String accountNumber;

    @Column(length = 10)
    private String bankCode;

    @Column(length = 100)
    private String customerName;

    private LocalDateTime dueDate;

    @Column(length = 20)
    private String refundStatus; // NONE, PENDING, FAILED, PARTIAL_FAILED, COMPLETED

    @Column(columnDefinition = "boolean default false")
    private boolean expired;

    @Column(length = 20)
    private String settlementStatus; // INCOMPLETED, COMPLETED

    @Embedded
    private RefundReceiveAccount refundReceiveAccount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Builder
    public VirtualAccount(String accountType, String accountNumber, String bankCode,
                          String customerName, LocalDateTime dueDate, String refundStatus,
                          boolean expired, String settlementStatus,
                          RefundReceiveAccount refundReceiveAccount) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.bankCode = bankCode;
        this.customerName = customerName;
        this.dueDate = dueDate;
        this.refundStatus = refundStatus;
        this.expired = expired;
        this.settlementStatus = settlementStatus;
        this.refundReceiveAccount = refundReceiveAccount;
    }
}