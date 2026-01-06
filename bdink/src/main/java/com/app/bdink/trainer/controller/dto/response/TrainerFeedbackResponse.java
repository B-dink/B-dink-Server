package com.app.bdink.trainer.controller.dto.response;

/**
 * 트레이너 피드백 조회 응답 DTO.
 */
public record TrainerFeedbackResponse(
        Long sessionId,
        String feedback
) {
    public static TrainerFeedbackResponse of(Long sessionId, String feedback) {
        return new TrainerFeedbackResponse(sessionId, feedback);
    }
}
