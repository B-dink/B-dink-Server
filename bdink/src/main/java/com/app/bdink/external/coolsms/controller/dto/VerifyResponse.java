package com.app.bdink.external.coolsms.controller.dto;

public record VerifyResponse(
        boolean isVerified
) {
    public static VerifyResponse from(boolean isVerified){
        return new VerifyResponse(isVerified);
    }
}
