package com.app.bdink.lecture.controller.dto.response;

import com.app.bdink.lecture.entity.Lecture;

public record LectureResponse(
    String title,
    String lectureTime,
    String progress
) {
    public static LectureResponse from(final Lecture lecture, final double watchProgress) {
        String progressStr = watchProgress >= 90 ? "완강" : Math.round(watchProgress) + "%";;
        return new LectureResponse(lecture.getTitle(), lecture.getTime().toString(), progressStr);
    }
}
