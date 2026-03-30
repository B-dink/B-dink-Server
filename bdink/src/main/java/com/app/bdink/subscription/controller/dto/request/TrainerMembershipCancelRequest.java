package com.app.bdink.subscription.controller.dto.request;

import java.time.LocalDate;

public record TrainerMembershipCancelRequest(
        LocalDate canceledDate
) {
}
