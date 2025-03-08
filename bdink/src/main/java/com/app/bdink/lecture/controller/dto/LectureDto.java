package com.app.bdink.lecture.controller.dto;

import java.time.LocalTime;

public record LectureDto(
        String title,
        int hour, // TODO: 영상 시간 어떻게 줄지 회의.
        int min,
        int seconds

) {
    public LocalTime convertToLocalTime(){
        return LocalTime.of(hour, min, seconds);
    }
}
