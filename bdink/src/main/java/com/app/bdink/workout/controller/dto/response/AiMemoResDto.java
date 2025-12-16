package com.app.bdink.workout.controller.dto.response;

import java.util.List;

public record AiMemoResDto(
        String workoutName,
        String workoutMemo,
        List<WorkoutDailyExerciseResDto> sessions
) {
}