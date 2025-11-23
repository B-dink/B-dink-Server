package com.app.bdink.workout.controller.dto.request;

import java.util.List;

public record WorkoutSessionSaveReqDto(
        String memo,
        List<PerformedExerciseSaveReqDto> performedExercises
) {
}
