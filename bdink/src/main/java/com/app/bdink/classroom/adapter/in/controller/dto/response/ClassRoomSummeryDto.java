package com.app.bdink.classroom.adapter.in.controller.dto.response;

public record ClassRoomSummeryDto(
    int chapterCount, // 챕터 개수
    int lectureCount, // 총 영상(강의) 개수
    int totalLectureHour, // 총 영상 길이(시간)
    int totalLectureMinute // 총 영상 길이(분)
) {
    public static ClassRoomSummeryDto of(final int chapterCount, final int lectureCount, final int totalLectureTime) {
        return new ClassRoomSummeryDto(
            chapterCount,
            lectureCount,
            totalLectureTime / 60,
            totalLectureTime % 60
        );
    }
}
