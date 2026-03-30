package com.app.bdink.trainermembership.controller.dto.response;

import com.app.bdink.trainermembership.entity.TrainerMembershipPlan;

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
