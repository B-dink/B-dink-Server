package com.app.bdink.subscription.controller.dto.response;

import com.app.bdink.subscription.entity.TrainerMembershipPlan;

public record TrainerMembershipPlanResponse(
        Long id,
        String name,
        Integer billingCycleMonths,
        Integer price,
        String description,
        Boolean active
) {
    public static TrainerMembershipPlanResponse from(TrainerMembershipPlan trainerMembershipPlan) {
        return new TrainerMembershipPlanResponse(
                trainerMembershipPlan.getId(),
                trainerMembershipPlan.getName(),
                trainerMembershipPlan.getBillingCycleMonths(),
                trainerMembershipPlan.getPrice(),
                trainerMembershipPlan.getDescription(),
                trainerMembershipPlan.getActive()
        );
    }
}
