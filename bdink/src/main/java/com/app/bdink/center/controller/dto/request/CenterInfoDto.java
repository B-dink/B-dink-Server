package com.app.bdink.center.controller.dto.request;

import com.app.bdink.center.entity.Center;

public record CenterInfoDto (
        String centerName,
        String centerAddress
//        String centerQrToken
//        Long centerQrTokenExpiredAt
){
    public static CenterInfoDto of(Center center){
        return new CenterInfoDto(
                center.getName(),
                center.getAddress()
//                center.getQrToken()
//                center.getQrTokenExpiredAt()
        );

    }
}
