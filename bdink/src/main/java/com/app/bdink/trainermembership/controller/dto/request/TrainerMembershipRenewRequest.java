package com.app.bdink.trainermembership.controller.dto.request;

import java.time.LocalDate;

public record TrainerMembershipRenewRequest(
        LocalDate paymentDate
) {
}
