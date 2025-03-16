package com.app.bdink.classroom.controller.dto.response;

import com.app.bdink.classroom.domain.ChapterSummary;
import com.app.bdink.classroom.entity.ClassRoom;

public record AllClassRoomResponse(
        Long id,
        String title,
        String instructor,
        int totalLectureCount
) {
    public static AllClassRoomResponse from(final ClassRoom classRoom, final ChapterSummary chapterSummary){
        return new AllClassRoomResponse(
                classRoom.getId(),
                classRoom.getTitle(),
                classRoom.getInstructor().getMember().getName(),
                chapterSummary.getTotalLectureCount()
        );
    }
}
