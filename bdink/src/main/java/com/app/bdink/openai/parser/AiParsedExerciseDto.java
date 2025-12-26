package com.app.bdink.openai.parser;

import com.app.bdink.workout.controller.dto.request.WorkoutSetSaveReqDto;
import com.app.bdink.workout.controller.dto.response.WorkoutDailySetResDto;

import java.util.List;

public record AiParsedExerciseDto(
        // 후보 리스트 기반으로 선택된 운동 ID (매칭 실패 시 null)
        Long exerciseId,
        String exerciseName,
        List<WorkoutDailySetResDto> sets
) {
}
