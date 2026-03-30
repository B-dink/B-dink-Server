package com.app.bdink.subscription.controller.dto.request;

import java.time.LocalDate;

public record TrainerMembershipCreateRequest(
        Long trainerMembershipPlanId,
        LocalDate paymentDate,
        boolean autoRenew
) {
}
