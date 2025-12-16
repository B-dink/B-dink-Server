package com.app.bdink.workout.controller.dto.response;

public record WeeklyVolumeCompareResDto(
        long lastWeekVolume,
        long thisWeekVolume,
        long diffVolume,
        int percentChange, // 변화율의 절댓값
        boolean increased, // true (증가), false(감소 or 동일)
        MonthlyWorkoutCountCompareResDto monthlyWorkoutCount
) {
}
