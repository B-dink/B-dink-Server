package com.app.bdink.trainer.controller.dto.response;

import com.app.bdink.classroom.domain.Career;
import com.app.bdink.trainer.entity.Trainer;
import com.app.bdink.trainer.entity.TrainerStatus;

/**
 * 트레이너 조회 응답 DTO.
 */
public record TrainerResponse(
        Long id,
        Long centerId,
        Long memberId,
        String name,
        Career career,
        String intro,
        String profileImage,
        TrainerStatus status
) {
    public static TrainerResponse from(Trainer trainer) {
        return new TrainerResponse(
                trainer.getId(),
                trainer.getCenter().getId(),
                trainer.getMember().getId(),
                trainer.getName(),
                trainer.getCareer(),
                trainer.getIntro(),
                trainer.getProfileImage(),
                trainer.getStatus()
        );
    }
}
