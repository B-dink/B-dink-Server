package com.app.bdink.lecture.controller.dto.response;

import com.app.bdink.external.aws.lambda.domain.Media;
import com.app.bdink.lecture.controller.dto.LectureIdInfoDto;
import com.app.bdink.lecture.entity.Lecture;

public record LectureCdnUrlResDto(
        String url,
        String lectureTitle,
        String InstructorName,
        Long prevLectureId,
        Long nextLectureId
) {
    public static LectureCdnUrlResDto from(final Lecture lecture, final Media media, final LectureIdInfoDto lectureIdInfoDto) {

        return new LectureCdnUrlResDto(
            media.getM3u8720Link(),
            lecture.getTitle(),
            lecture.getClassRoom().getInstructor().getMember().getName(),
            lectureIdInfoDto.prevLectureId(),
            lectureIdInfoDto.nextLectureId()
        );

    }
}
