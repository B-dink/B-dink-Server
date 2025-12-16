package com.app.bdink.workout.controller.dto.response;

import java.util.List;

public record WorkoutDailyDetailResDto(
        String date,
        String workoutName,
        String workoutMemo,
        long sessionId,
        List<WorkoutDailyExerciseResDto> sessions
) {
}
