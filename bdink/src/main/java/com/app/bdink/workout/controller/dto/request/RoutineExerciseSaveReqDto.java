package com.app.bdink.workout.controller.dto.request;

import com.app.bdink.workout.entity.RoutineSetTemplate;

import java.util.List;

public record RoutineExerciseSaveReqDto(
        Long exerciseId,
        String memo,
        int orderIndex,
        List<RoutineSetTemplateSaveReqDto> sets
) {
}
