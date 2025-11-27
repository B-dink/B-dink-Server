package com.app.bdink.workout.controller.dto.response;

import com.app.bdink.workout.controller.dto.ExercisePart;
import com.app.bdink.workout.entity.Exercise;

public record ExerciseSearchResDto(
        Long exerciseId,
        String name,
        String pictureUrl,
        ExercisePart part
) {
    public static ExerciseSearchResDto of(Exercise exercise) {
        return new ExerciseSearchResDto(
                exercise.getId(),
                exercise.getName(),
                exercise.getPictureUrl(),
                exercise.getPart()
        );
    }
}
