package com.app.bdink.trainermember.controller.dto.response;

public record TrainerMemberVerify(
        boolean isTrainerMember,
        String trainerName
) {
}
