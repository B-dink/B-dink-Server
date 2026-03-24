package com.app.bdink.workout.controller.dto.request;

import com.app.bdink.workout.entity.WorkoutFeedbackMediaType;

public record WorkoutFeedbackMediaReqDto(
        WorkoutFeedbackMediaType mediaType,
        String url,
        Integer order,
        String thumbnailUrl
) {
}
