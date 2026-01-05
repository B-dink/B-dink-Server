package com.app.bdink.trainer.controller.dto.request;

import com.app.bdink.classroom.domain.Career;

/**
 * 트레이너 생성 요청 DTO.
 * name은 Member.name으로 자동 설정되므로 전달하지 않는다.
 */
public record TrainerCreateRequest(
        Long centerId,
        Career career,
        String intro
) {
}
