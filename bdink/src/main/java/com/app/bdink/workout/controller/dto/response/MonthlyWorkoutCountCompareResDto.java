package com.app.bdink.workout.controller.dto.response;

public record MonthlyWorkoutCountCompareResDto(
        int thisMonthCount, // 이번달 운동 횟수
        int lastMonthCount, // 저번달 운동 횟수
        int diffCount,  // 이번달 - 저번달 운동 횟수
        int monPercentChange,   // 운동횟수 변화율
        boolean monIncreased,   // 운동횟수 증감
        int daysInThisMonth // 이번달 총 일 수
) {
}
