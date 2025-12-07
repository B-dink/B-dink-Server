package com.app.bdink.workout.controller.dto.response;

import com.app.bdink.workout.entity.RecommendedRoutine;

public record RecommendedRoutineAllResDto(
        Long routineId,
        String routineTitle,
        String routineThumbnailUrl
) {
    public static RecommendedRoutineAllResDto of(final RecommendedRoutine routine) {
        return new RecommendedRoutineAllResDto(
                routine.getId(),
                routine.getTitle(),
                routine.getThumbnailUrl()
        );
    }
}
