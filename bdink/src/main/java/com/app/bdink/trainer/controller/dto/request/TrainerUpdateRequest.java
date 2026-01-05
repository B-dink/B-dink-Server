package com.app.bdink.trainer.controller.dto.request;

import com.app.bdink.classroom.domain.Career;

/**
 * 트레이너 수정 요청 DTO.
 * null 값은 기존 값을 유지한다.
 */
public record TrainerUpdateRequest(
        String name,
        Career career,
        String intro
) {
}
