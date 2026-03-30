package com.app.bdink.subscription.controller.dto.response;

import com.app.bdink.subscription.entity.TrainerMembership;

import java.time.LocalDate;

public record TrainerMembershipResponse(
        Long id,
        Long trainerId,
        Long trainerMembershipPlanId,
        String trainerMembershipPlanName,
        Integer billingCycleMonths,
        Integer price,
        String trainerMembershipStatus,
        LocalDate startedDate,
        LocalDate nextBillingDate,
        LocalDate expiredDate,
        boolean autoRenew,
        LocalDate canceledDate
) {
    public static TrainerMembershipResponse from(TrainerMembership membership) {
        return new TrainerMembershipResponse(
                membership.getId(),
                membership.getTrainer().getId(),
                membership.getTrainerMembershipPlan().getId(),
                membership.getTrainerMembershipPlan().getName(),
                membership.getTrainerMembershipPlan().getBillingCycleMonths(),
                membership.getTrainerMembershipPlan().getPrice(),
                membership.getTrainerMembershipStatus().name(),
                membership.getStartedDate(),
                membership.getNextBillingDate(),
                membership.getExpiredDate(),
                membership.isAutoRenew(),
                membership.getCanceledDate()
        );
    }
}
