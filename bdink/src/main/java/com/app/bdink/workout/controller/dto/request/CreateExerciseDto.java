package com.app.bdink.workout.controller.dto.request;

public record CreateExerciseDto(
        String ExerciseVideoKey,
        String ExercisePictureKey,
        ExerciseReqDto exerciseReqDto
) {
    public static CreateExerciseDto of(
            String exerciseVideoKey,
            String exercisePictureKey,
            ExerciseReqDto exerciseReqDto
    ) {
        return new CreateExerciseDto(exerciseVideoKey, exercisePictureKey, exerciseReqDto);
    }
}
