package com.app.bdink.workout.controller.dto.response;

import java.util.List;

public record RecommendedRoutineSessionResDto(
        Long exerciseId,
        String exerciseName,
        String pictureUrl,
//        String memo,
        List<RecommendedRoutineSetResDto> sets
) {
}
