package com.app.bdink.lecture.controller.dto.response;

import com.app.bdink.lecture.entity.Lecture;

public record LectureResponse(
    String title,
    String lectureTime
) {
    public static LectureResponse from(final Lecture lecture) {
        return new LectureResponse(lecture.getTitle(), lecture.getTime().toString());
    }
}
