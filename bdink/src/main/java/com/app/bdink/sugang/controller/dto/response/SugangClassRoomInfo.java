package com.app.bdink.sugang.controller.dto.response;

import com.app.bdink.sugang.entity.Sugang;

public record SugangClassRoomInfo (
        Long sugangId,
        Long classRoomId,
        String classRoomTitle,
        String classRoomThumnail,
        String InstructorName,
        Double progressClassRoom
)
{
    public static SugangClassRoomInfo of(Sugang sugang){
        double roundedProgress = Math.round(sugang.getProgressPercent() * 100.0) / 100.0;
        return new SugangClassRoomInfo(
                sugang.getId(),
                sugang.getClassRoomEntity().getId(),
                sugang.getClassRoomEntity().getTitle(),
                sugang.getClassRoomEntity().getThumbnail(),
                sugang.getClassRoomEntity().getInstructor().getMember().getName(),
                roundedProgress
        );
    }
}
