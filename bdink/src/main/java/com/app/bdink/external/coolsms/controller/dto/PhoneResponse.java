package com.app.bdink.external.coolsms.controller.dto;

import com.app.bdink.external.coolsms.entity.PhoneVerify;

import java.util.UUID;

public record PhoneResponse(
        String transactionId,
        String phone,
        String code
)
{
    public static PhoneResponse from(final PhoneVerify phoneVerify){
        return new PhoneResponse(
                phoneVerify.getId().toString(),
                phoneVerify.getPhone(),
                phoneVerify.getVerifyCode());
    }
}
