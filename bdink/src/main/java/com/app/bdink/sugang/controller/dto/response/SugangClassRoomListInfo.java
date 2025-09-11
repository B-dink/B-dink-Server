package com.app.bdink.sugang.controller.dto.response;
import com.app.bdink.sugang.entity.Sugang;

public record SugangClassRoomListInfo(
        /**
         * 필요한 데이터
         * - 클래스룸 id
         * - 클래스룸 제목
         * - 클래스룸 전체 진행률
         * - 클래스룸 썸네일
         * - 강사 이름
         */
        Long classRoomId,
        String classRoomTitle,
        double classRoomProgress,
        String classRoomThumbnail,
        String InstructorName
) {
    public static SugangClassRoomListInfo of(Sugang sugang) {
        return new SugangClassRoomListInfo(
                sugang.getClassRoomEntity().getId(),
                sugang.getClassRoomEntity().getTitle(),
                sugang.getProgressPercent(),
                sugang.getClassRoomEntity().getThumbnail(),
                sugang.getClassRoomEntity().getInstructor().getMember().getName()
        );
    }
}
