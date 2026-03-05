package com.app.bdink.workout.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record WorkoutFeedbackResDto(
        Long sessionId,
        String content,
        List<WorkoutFeedbackMediaResDto> media,
        LocalDateTime createdAt
) {
}
