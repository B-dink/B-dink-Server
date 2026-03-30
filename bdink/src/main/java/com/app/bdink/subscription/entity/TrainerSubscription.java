package com.app.bdink.subscription.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.trainer.entity.Trainer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "trainer_subscription",
        indexes = {
                @Index(name = "idx_trainer_subscription_next_billing_date", columnList = "nextBillingDate"),
                @Index(name = "idx_trainer_subscription_expired_date", columnList = "expiredDate")
        }
)
public class TrainerSubscription extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TrainerMembershipStatus subscriptionStatus;

    @Column(nullable = false)
    private LocalDate startedDate;

    @Column(nullable = false)
    private LocalDate nextBillingDate;

    @Column(nullable = false)
    private LocalDate expiredDate;

    @Column(nullable = false)
    private boolean autoRenew;

    private LocalDate canceledDate;

    @Builder
    public TrainerSubscription(Trainer trainer, SubscriptionPlan subscriptionPlan, TrainerMembershipStatus subscriptionStatus,
                               LocalDate startedDate, LocalDate nextBillingDate, LocalDate expiredDate,
                               boolean autoRenew, LocalDate canceledDate) {
        this.trainer = trainer;
        this.subscriptionPlan = subscriptionPlan;
        this.subscriptionStatus = subscriptionStatus == null ? TrainerMembershipStatus.ACTIVE : subscriptionStatus;
        this.startedDate = startedDate;
        this.nextBillingDate = nextBillingDate;
        this.expiredDate = expiredDate;
        this.autoRenew = autoRenew;
        this.canceledDate = canceledDate;
    }

    public static TrainerSubscription create(Trainer trainer, SubscriptionPlan subscriptionPlan,
                                             LocalDate paymentDate, boolean autoRenew) {
        LocalDate expiredDate = paymentDate.plusMonths(subscriptionPlan.getBillingCycleMonths());
        LocalDate nextBillingDate = expiredDate.plusDays(1);
        return TrainerSubscription.builder()
                .trainer(trainer)
                .subscriptionPlan(subscriptionPlan)
                .subscriptionStatus(TrainerMembershipStatus.ACTIVE)
                .startedDate(paymentDate)
                .nextBillingDate(nextBillingDate)
                .expiredDate(expiredDate)
                .autoRenew(autoRenew)
                .build();
    }

    public void renew(LocalDate paymentDate) {
        // 이용 마지막 날과 다음 결제 필요일을 분리해 하루 차이로 관리한다.
        LocalDate nextExpiredDate = paymentDate.plusMonths(subscriptionPlan.getBillingCycleMonths());
        this.expiredDate = nextExpiredDate;
        this.nextBillingDate = nextExpiredDate.plusDays(1);
        this.subscriptionStatus = TrainerMembershipStatus.ACTIVE;
        this.canceledDate = null;
    }

    public void cancel(LocalDate canceledDate) {
        this.subscriptionStatus = TrainerMembershipStatus.CANCELED;
        this.autoRenew = false;
        this.canceledDate = canceledDate;
    }

    public void expire(LocalDate expiredDate) {
        this.subscriptionStatus = TrainerMembershipStatus.EXPIRED;
        this.expiredDate = expiredDate;
        this.autoRenew = false;
    }

    public void markPaymentFailed() {
        this.subscriptionStatus = TrainerMembershipStatus.PAYMENT_FAILED;
    }

    public boolean isBillingDue(LocalDate today) {
        return autoRenew
                && subscriptionStatus == TrainerMembershipStatus.ACTIVE
                && !nextBillingDate.isAfter(today);
    }
}
