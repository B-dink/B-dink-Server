package com.app.bdink.trainer.controller.dto.request;

public record TrainerProfileCompleteRequest(
        Long centerId,
        String trainerName,
        String intro
) {
}
