package com.app.bdink.trainermembership.entity;

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
        name = "trainer_membership",
        indexes = {
                @Index(name = "idx_trainer_membership_next_billing_date", columnList = "nextBillingDate"),
                @Index(name = "idx_trainer_membership_expired_date", columnList = "expiredDate")
        }
)
public class TrainerMembership extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_membership_plan_id", nullable = false)
    private TrainerMembershipPlan trainerMembershipPlan;

    @Enumerated(EnumType.STRING)
    @Column(name = "trainer_membership_status", nullable = false, length = 30)
    private TrainerMembershipStatus trainerMembershipStatus;

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
    public TrainerMembership(Trainer trainer, TrainerMembershipPlan trainerMembershipPlan, TrainerMembershipStatus trainerMembershipStatus,
                             LocalDate startedDate, LocalDate nextBillingDate, LocalDate expiredDate,
                             boolean autoRenew, LocalDate canceledDate) {
        this.trainer = trainer;
        this.trainerMembershipPlan = trainerMembershipPlan;
        this.trainerMembershipStatus = trainerMembershipStatus == null ? TrainerMembershipStatus.ACTIVE : trainerMembershipStatus;
        this.startedDate = startedDate;
        this.nextBillingDate = nextBillingDate;
        this.expiredDate = expiredDate;
        this.autoRenew = autoRenew;
        this.canceledDate = canceledDate;
    }

    public static TrainerMembership create(Trainer trainer, TrainerMembershipPlan trainerMembershipPlan,
                                           LocalDate paymentDate, boolean autoRenew) {
        LocalDate expiredDate = paymentDate.plusMonths(trainerMembershipPlan.getBillingCycleMonths());
        LocalDate nextBillingDate = expiredDate.plusDays(1);
        return TrainerMembership.builder()
                .trainer(trainer)
                .trainerMembershipPlan(trainerMembershipPlan)
                .trainerMembershipStatus(TrainerMembershipStatus.ACTIVE)
                .startedDate(paymentDate)
                .nextBillingDate(nextBillingDate)
                .expiredDate(expiredDate)
                .autoRenew(autoRenew)
                .build();
    }

    public void renew(LocalDate paymentDate) {
        // 이용 마지막 날과 다음 결제 필요일을 분리해 하루 차이로 관리한다.
        LocalDate nextExpiredDate = paymentDate.plusMonths(trainerMembershipPlan.getBillingCycleMonths());
        this.expiredDate = nextExpiredDate;
        this.nextBillingDate = nextExpiredDate.plusDays(1);
        this.trainerMembershipStatus = TrainerMembershipStatus.ACTIVE;
        this.canceledDate = null;
    }

    public void cancel(LocalDate canceledDate) {
        this.trainerMembershipStatus = TrainerMembershipStatus.CANCELED;
        this.autoRenew = false;
        this.canceledDate = canceledDate;
    }

    public void expire(LocalDate expiredDate) {
        this.trainerMembershipStatus = TrainerMembershipStatus.EXPIRED;
        this.expiredDate = expiredDate;
        this.autoRenew = false;
    }

    public void markPaymentFailed() {
        this.trainerMembershipStatus = TrainerMembershipStatus.PAYMENT_FAILED;
    }

    public boolean isBillingDue(LocalDate today) {
        return autoRenew
                && trainerMembershipStatus == TrainerMembershipStatus.ACTIVE
                && !nextBillingDate.isAfter(today);
    }
}
