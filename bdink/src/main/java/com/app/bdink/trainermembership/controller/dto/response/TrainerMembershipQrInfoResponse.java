package com.app.bdink.trainermembership.controller.dto.response;

import com.app.bdink.trainermembership.entity.TrainerMembership;
import com.app.bdink.trainer.entity.Trainer;

import java.time.LocalDate;

public record TrainerMembershipQrInfoResponse(
        String qrImageUrl,
        LocalDate expiredDate
) {
    public static TrainerMembershipQrInfoResponse from(Trainer trainer, TrainerMembership membership) {
        return new TrainerMembershipQrInfoResponse(
                trainer.getQrImageUrl(),
                membership.getExpiredDate()
        );
    }
}
