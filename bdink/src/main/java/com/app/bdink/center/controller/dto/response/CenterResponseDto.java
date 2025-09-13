package com.app.bdink.center.controller.dto.response;

import com.app.bdink.center.entity.Center;

public record CenterResponseDto (
        String centerName,
        String centerAddress
){
    public static CenterResponseDto of(Center center){
        return new CenterResponseDto(
                center.getName(),
                center.getAddress()
        );

    }
}
