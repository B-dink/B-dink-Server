package com.app.bdink.trainermembership.controller.dto.response;

import com.app.bdink.trainermembership.entity.TrainerMembershipPlan;

public record TrainerMembershipPlanResponse(
        Long id,
        String name,
        Integer billingCycleMonths,
        Integer price,
        Integer discountPrice,
        Integer monthlyPrice,
        String tag,
        Integer discountRate,
        String description,
        Boolean active,
        Boolean isAnnualProduct
) {
    public static TrainerMembershipPlanResponse from(TrainerMembershipPlan trainerMembershipPlan) {
        boolean isAnnualProduct = Boolean.TRUE.equals(trainerMembershipPlan.getIsAnnualProduct());

        return new TrainerMembershipPlanResponse(
                trainerMembershipPlan.getId(),
                trainerMembershipPlan.getName(),
                trainerMembershipPlan.getBillingCycleMonths(),
                trainerMembershipPlan.getPrice(),
                isAnnualProduct ? trainerMembershipPlan.getDiscountPrice() : null,
                isAnnualProduct ? trainerMembershipPlan.getMonthlyPrice() : null,
                trainerMembershipPlan.getTag().name(),
                trainerMembershipPlan.getDiscountRate(),
                trainerMembershipPlan.getDescription(),
                trainerMembershipPlan.getActive(),
                trainerMembershipPlan.getIsAnnualProduct()
        );
    }
}
