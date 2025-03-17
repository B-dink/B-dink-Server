package com.app.bdink.classroom.adapter.in.controller.dto.response;

import com.app.bdink.classroom.domain.ChapterSummary;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;

public record AllClassRoomResponse(
        Long id,
        String title,
        String instructor,
        int totalLectureCount
) {
    public static AllClassRoomResponse from(final ClassRoomEntity classRoomEntity, final ChapterSummary chapterSummary){
        return new AllClassRoomResponse(
                classRoomEntity.getId(),
                classRoomEntity.getTitle(),
                classRoomEntity.getInstructor().getMember().getName(),
                chapterSummary.getTotalLectureCount()
        );
    }
}
