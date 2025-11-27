package com.app.bdink.workout.controller.dto.request;

import java.util.List;

public record RecommendedRoutineSaveReqDto(
        String title,
        String description,
        List<RoutineExerciseSaveReqDto> sessions
) {
}
