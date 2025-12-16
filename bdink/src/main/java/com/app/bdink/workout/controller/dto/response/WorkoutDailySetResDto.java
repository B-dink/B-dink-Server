package com.app.bdink.workout.controller.dto.response;

public record WorkoutDailySetResDto(
        int setNumber,
        int reps,
        int weight
) {
}
