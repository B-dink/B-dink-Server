package com.app.bdink.lecture.controller.dto.response;

import com.app.bdink.lecture.entity.Lecture;

import java.time.LocalTime;

public record LectureInfo(
        String title,

        String mediaLink,
        String lectureTime
) {
    public static LectureInfo from(final Lecture lecture){
        return new LectureInfo(
                lecture.getTitle(),
                lecture.getMediaLink(),
                makeTimeString(lecture.getTime())
        );
    }

    //TODO: 만약 시간과 관련된 로직이 증가한다면 시간 관련 도메인으로 따로 빼기.
    public static String makeTimeString(final LocalTime time){
        return time.getHour()+"시간 "+time.getMinute()+"분 "+time.getSecond()+"초";
    }
}
