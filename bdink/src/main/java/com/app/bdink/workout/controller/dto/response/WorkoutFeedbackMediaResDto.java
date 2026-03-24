package com.app.bdink.workout.controller.dto.response;

import com.app.bdink.workout.entity.WorkoutFeedbackMediaType;

public record WorkoutFeedbackMediaResDto(
        WorkoutFeedbackMediaType mediaType,
        String url,
        Integer order,
        String thumbnailUrl
) {
}
