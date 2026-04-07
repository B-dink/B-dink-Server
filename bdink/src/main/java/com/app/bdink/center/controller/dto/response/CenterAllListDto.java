package com.app.bdink.center.controller.dto.response;

import com.app.bdink.center.entity.Center;

public record CenterAllListDto(
    Long centerId,
    String centerName,
    String centerAddress
) {
    public static CenterAllListDto of(Center center){
        return new CenterAllListDto(
                center.getId(),
                center.getName(),
                center.getAddress()
        );
    }
}
