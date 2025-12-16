package com.app.bdink.workout.controller.dto.request;

public record RoutineSetTemplateSaveReqDto(
        int setNumber,
        int reps,
        int weight
) {
}
