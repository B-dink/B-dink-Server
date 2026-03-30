package com.app.bdink.subscription.controller.dto.request;

import java.time.LocalDate;

public record TrainerMembershipCreateRequest(
        Long subscriptionPlanId,
        LocalDate paymentDate,
        boolean autoRenew
) {
}
