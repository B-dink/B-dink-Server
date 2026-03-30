package com.app.bdink.subscription.controller.dto.request;

import java.time.LocalDate;

public record TrainerSubscriptionCreateRequest(
        Long trainerId,
        Long subscriptionPlanId,
        LocalDate paymentDate,
        boolean autoRenew
) {
}
