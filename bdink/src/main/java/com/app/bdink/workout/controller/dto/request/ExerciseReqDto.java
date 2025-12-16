package com.app.bdink.workout.controller.dto.request;

import com.app.bdink.workout.controller.dto.ExercisePart;

public record ExerciseReqDto(
        String ExerciseName,
        String ExerciseDescription,
        ExercisePart ExercisePart
)
{
}
