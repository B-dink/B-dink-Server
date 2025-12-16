package com.app.bdink.openai.parser;

import com.app.bdink.workout.controller.dto.request.WorkoutSetSaveReqDto;
import com.app.bdink.workout.controller.dto.response.WorkoutDailySetResDto;

import java.util.List;

public record AiParsedExerciseDto(
        String exerciseName,
        List<WorkoutDailySetResDto> sets
) {
}
