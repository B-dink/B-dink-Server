package com.app.bdink.trainermember.controller.dto.request;

/**
 * 트레이너 소속 멤버 수정 요청 DTO.
 * 멤버 이동(트레이너 변경) 용도로 사용한다.
 */
public record TrainerMemberUpdateRequest(
        Long trainerId
) {
}
