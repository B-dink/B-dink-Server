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
    private LocalDate expiredDate;

    @Builder
    public TrainerMembership(Trainer trainer, TrainerMembershipPlan trainerMembershipPlan, TrainerMembershipStatus trainerMembershipStatus,
                             LocalDate startedDate, LocalDate expiredDate) {
        this.trainer = trainer;
        this.trainerMembershipPlan = trainerMembershipPlan;
        this.trainerMembershipStatus = trainerMembershipStatus == null ? TrainerMembershipStatus.ACTIVE : trainerMembershipStatus;
        this.startedDate = startedDate;
        this.expiredDate = expiredDate;
    }

    public static TrainerMembership create(Trainer trainer, TrainerMembershipPlan trainerMembershipPlan,
                                           LocalDate paymentDate) {
        LocalDate expiredDate = paymentDate.plusMonths(trainerMembershipPlan.getBillingCycleMonths());
        return TrainerMembership.builder()
                .trainer(trainer)
                .trainerMembershipPlan(trainerMembershipPlan)
                .trainerMembershipStatus(TrainerMembershipStatus.ACTIVE)
                .startedDate(paymentDate)
                .expiredDate(expiredDate)
                .build();
    }

    public void expire(LocalDate expiredDate) {
        this.trainerMembershipStatus = TrainerMembershipStatus.EXPIRED;
        this.expiredDate = expiredDate;
    }
}
