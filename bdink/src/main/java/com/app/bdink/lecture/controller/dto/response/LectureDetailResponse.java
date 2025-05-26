package com.app.bdink.lecture.controller.dto.response;

import com.app.bdink.lecture.entity.Lecture;

public record LectureDetailResponse(
        Long lectureId,
        String lectureTitle,
        String lectureTime
) {
    public static LectureDetailResponse from(final Lecture lecture) {
        return new LectureDetailResponse(lecture.getId(), lecture.getTitle(), lecture.getTime().toString());
    }
}

