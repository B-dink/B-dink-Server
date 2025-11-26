package com.app.bdink.workout.controller.dto.request;

import java.util.List;

public record PerformedExerciseSaveReqDto(
        Long exerciseId,
        String memo,
        List<WorkoutSetSaveReqDto> sets
) {
}
