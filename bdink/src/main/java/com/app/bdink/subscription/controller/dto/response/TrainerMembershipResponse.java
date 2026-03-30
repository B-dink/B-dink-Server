package com.app.bdink.subscription.controller.dto.response;

import com.app.bdink.subscription.entity.TrainerSubscription;

import java.time.LocalDate;

public record TrainerMembershipResponse(
        Long id,
        Long trainerId,
        Long subscriptionPlanId,
        String subscriptionPlanName,
        Integer billingCycleMonths,
        Integer price,
        String trainerMembershipStatus,
        LocalDate startedDate,
        LocalDate nextBillingDate,
        LocalDate expiredDate,
        boolean autoRenew,
        LocalDate canceledDate
) {
    public static TrainerMembershipResponse from(TrainerSubscription subscription) {
        return new TrainerMembershipResponse(
                subscription.getId(),
                subscription.getTrainer().getId(),
                subscription.getSubscriptionPlan().getId(),
                subscription.getSubscriptionPlan().getName(),
                subscription.getSubscriptionPlan().getBillingCycleMonths(),
                subscription.getSubscriptionPlan().getPrice(),
                subscription.getSubscriptionStatus().name(),
                subscription.getStartedDate(),
                subscription.getNextBillingDate(),
                subscription.getExpiredDate(),
                subscription.isAutoRenew(),
                subscription.getCanceledDate()
        );
    }
}
