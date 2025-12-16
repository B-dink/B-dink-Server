package com.app.bdink.workout.controller.dto.response;

import java.util.List;

public record ExerciseVersionResDto(
        String version,
        boolean isVersionChecked,
        List<ExerciseResDto> exercises
) {
}
