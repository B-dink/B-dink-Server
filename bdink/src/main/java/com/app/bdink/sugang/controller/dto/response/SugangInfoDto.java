package com.app.bdink.sugang.controller.dto.response;

import com.app.bdink.sugang.controller.dto.SugangStatus;
import com.app.bdink.sugang.entity.Sugang;

import java.time.LocalDate;

public record SugangInfoDto(
        Long sugangId,
        Long memberId,
        LocalDate expiredDate,
        Long classRoomEntityId,
        SugangStatus sugangStatus

) {
    public static SugangInfoDto of(Sugang sugang){
        return new SugangInfoDto(
                sugang.getId(),
                sugang.getMember().getId(),
                sugang.getExpiredDate(),
                sugang.getClassRoomEntity().getId(),
                sugang.getSugangStatus()
        );

    }
}
