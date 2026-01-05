package com.app.bdink.trainer.controller.dto.request;

/**
 * 트레이너 QR 인증 요청 DTO.
 */
public record TrainerQrVerifyRequest(
        String qrToken
) {
}
