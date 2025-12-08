package com.app.bdink.openai.parser;

import java.util.List;

public record AiParsedWorkoutDto(
        List<AiParsedExerciseDto> exercises
) {
}
