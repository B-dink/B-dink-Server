package com.app.bdink.trainermember.controller.dto.request;

/**
 * 트레이너 소속 멤버 생성 요청 DTO.
 */
public record TrainerMemberCreateRequest(
        Long trainerId,
        Long memberId
) {
}
