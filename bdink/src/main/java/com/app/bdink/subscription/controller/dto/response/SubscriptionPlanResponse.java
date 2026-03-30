package com.app.bdink.subscription.controller.dto.response;

import com.app.bdink.subscription.entity.SubscriptionPlan;

public record SubscriptionPlanResponse(
        Long id,
        String name,
        Integer billingCycleMonths,
        Integer price,
        String description,
        Boolean active
) {
    public static SubscriptionPlanResponse from(SubscriptionPlan subscriptionPlan) {
        return new SubscriptionPlanResponse(
                subscriptionPlan.getId(),
                subscriptionPlan.getName(),
                subscriptionPlan.getBillingCycleMonths(),
                subscriptionPlan.getPrice(),
                subscriptionPlan.getDescription(),
                subscriptionPlan.getActive()
        );
    }
}
