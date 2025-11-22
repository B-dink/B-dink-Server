package com.app.bdink.workout.controller.dto.response;

import com.app.bdink.workout.entity.Exercise;

public record ExerciseResDto(
        String ExerciseName,
        String ExerciseDescription,
        String ExerciseVideo,
        String ExercisePicture,
        String ExercisePart
) {
    public static ExerciseResDto of(final Exercise exercise) {
        return new ExerciseResDto(
                exercise.getName(),
                exercise.getDescription(),
                exercise.getVideoUrl(),
                exercise.getPictureUrl(),
                exercise.getPart().toString()
        );
    }
}