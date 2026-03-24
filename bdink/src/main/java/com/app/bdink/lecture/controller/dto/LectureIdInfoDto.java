package com.app.bdink.lecture.controller.dto;

import com.app.bdink.lecture.entity.Lecture;

public record LectureIdInfoDto(
        Long prevLectureId,
        Long nextLectureId
) {
    public static LectureIdInfoDto from(Long prevLectureId, Long nextLectureId) {
        return new LectureIdInfoDto(prevLectureId, nextLectureId);
    }
}
