package com.app.bdink.workout.controller.dto.response;

import java.util.List;

public record RecommendedRoutineDetailResDto(
        String workoutName,
        String routineThumbnailUrl,
        List<RecommendedRoutineSessionResDto> sessions
) {
}
