package com.app.bdink.subscription.controller.dto.response;

import com.app.bdink.subscription.entity.SubscriptionPlan;

public record TrainerMembershipPlanResponse(
        Long id,
        String name,
        Integer billingCycleMonths,
        Integer price,
        String description,
        Boolean active
) {
    public static TrainerMembershipPlanResponse from(SubscriptionPlan subscriptionPlan) {
        return new TrainerMembershipPlanResponse(
                subscriptionPlan.getId(),
                subscriptionPlan.getName(),
                subscriptionPlan.getBillingCycleMonths(),
                subscriptionPlan.getPrice(),
                subscriptionPlan.getDescription(),
                subscriptionPlan.getActive()
        );
    }
}
