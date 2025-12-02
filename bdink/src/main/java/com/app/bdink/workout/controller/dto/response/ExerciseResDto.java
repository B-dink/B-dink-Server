package com.app.bdink.workout.controller.dto.response;
import com.app.bdink.workout.controller.dto.ExercisePart;
import com.app.bdink.workout.entity.Exercise;

public record ExerciseResDto(
        Long ExerciseId,
        String ExerciseName,
        String ExerciseDescription,
        String ExerciseVideo,
        String ExercisePicture,
        ExercisePart ExercisePart
) {
    public static ExerciseResDto of(final Exercise exercise) {
        return new ExerciseResDto(
                exercise.getId(),
                exercise.getName(),
                exercise.getDescription(),
                exercise.getVideoUrl(),
                exercise.getPictureUrl(),
                exercise.getPart()
        );
    }
}