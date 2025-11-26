package com.app.bdink.workout.controller.dto.response;

import java.time.LocalDate;
import java.util.List;

public record WeeklyVolumeGraphResDto(
        /***
         * 이번 주 월요일 날짜
         * 지난 주 월요일 날짜
         * [월, 화 ~ 일] 순서
         */
        LocalDate thisWeekStart,
        LocalDate lastWeekStart,
        List<Long> thisWeekVolumes,
        List<Long> lastWeekVolumes
) {
}
