package com.app.bdink.sugang.controller.dto.response;

import com.app.bdink.external.kollus.entity.KollusMediaLink;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.sugang.entity.Sugang;

public record SugangClassRoomInfo (
        Long classRoomId,
        Long lectureId,
        String lectureTitle,
        double lectureProgress,
        String classRoomThumbnail,
        String InstructorName
//        Double progressClassRoom //클래스룸 전체 진행률
)
{
    public static SugangClassRoomInfo of(Sugang sugang, Lecture lecture, KollusMediaLink kollusMediaLink) {
//        double roundedProgress = Math.round(sugang.getProgressPercent() * 100.0) / 100.0;
        double watchProgress = kollusMediaLink != null ? kollusMediaLink.getPlaytimePercent() : 0.0;
        return new SugangClassRoomInfo(
                //todo: 수강말고 다른 엔티티에서 찾아야함.
                lecture.getClassRoom().getId(),
                lecture.getId(),
                lecture.getTitle(),
                watchProgress,
                sugang.getClassRoomEntity().getThumbnail(),
                sugang.getClassRoomEntity().getInstructor().getMember().getName()
        );
    }
}
