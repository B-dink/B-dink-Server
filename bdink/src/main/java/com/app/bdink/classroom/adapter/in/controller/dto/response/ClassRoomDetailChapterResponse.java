package com.app.bdink.classroom.adapter.in.controller.dto.response;

import com.app.bdink.chapter.entity.Chapter;
import com.app.bdink.lecture.controller.dto.response.LectureDetailResponse;

import java.util.List;

public record ClassRoomDetailChapterResponse (
    String chapterThumbnail,
    String title,
    List<LectureDetailResponse> lectureDetailResponses
)

{
    public static ClassRoomDetailChapterResponse from(final Chapter chapter, List<LectureDetailResponse> lectureDetailResponses) {
        return new ClassRoomDetailChapterResponse (
                chapter.getThumbnail(), chapter.getTitle(), lectureDetailResponses
        );
    }
}
