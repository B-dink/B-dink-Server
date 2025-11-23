package com.app.bdink.workout.controller.dto.request;

public record WorkoutSetSaveReqDto(
        Integer setNumber,
        Integer weight,
        Integer reps,
        Integer restTime
) {
}
