package com.app.bdink.classroom.controller.dto.response;

import com.app.bdink.lecture.entity.Chapter;
import java.util.List;

public record ChapterResponse(
    String title,
    List<LectureResponse> lectures
) {
    public static ChapterResponse of(final Chapter chapters) {
        return new ChapterResponse(chapters.getTitle(),
            chapters.getLectures().stream()
                .map(LectureResponse::from)
                .toList());
    }
}
