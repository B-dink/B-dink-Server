package com.app.bdink.workout.controller.dto.response;

import java.util.List;

public record WorkoutDailyExerciseResDto(
        Long exerciseId,
        String exerciseName,
        String pictureUrl,
        String memo,
        List<WorkoutDailySetResDto> sets
) {
}
