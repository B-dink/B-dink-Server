package com.app.bdink.center.controller.dto.request;

public record CenterQrDto(
        String qrToken
) {
    public static CenterQrDto of(String qrToken){
        return new CenterQrDto(qrToken);
    }
}
