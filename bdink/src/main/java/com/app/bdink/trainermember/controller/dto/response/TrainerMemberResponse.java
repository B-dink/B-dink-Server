package com.app.bdink.trainermember.controller.dto.response;

import com.app.bdink.trainermember.entity.TrainerMember;
import com.app.bdink.trainermember.entity.TrainerMemberStatus;

/**
 * 트레이너 소속 멤버 조회 응답 DTO.
 */
public record TrainerMemberResponse(
        Long id,
        Long trainerId,
        Long memberId,
        TrainerMemberStatus status
) {
    public static TrainerMemberResponse from(TrainerMember trainerMember) {
        return new TrainerMemberResponse(
                trainerMember.getId(),
                trainerMember.getTrainer().getId(),
                trainerMember.getMember().getId(),
                trainerMember.getStatus()
        );
    }
}
