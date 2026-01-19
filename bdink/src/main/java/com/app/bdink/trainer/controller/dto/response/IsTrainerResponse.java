package com.app.bdink.trainer.controller.dto.response;

/**
 * 로그인한 사용자가 트레이너인지 여부를 반환한다.
 */
public record IsTrainerResponse(
        boolean isTrainer
) {
    public static IsTrainerResponse from(boolean isTrainer) {
        return new IsTrainerResponse(isTrainer);
    }
}
