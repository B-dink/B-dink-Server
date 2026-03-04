package com.app.bdink.workout.controller.dto.request;

import com.app.bdink.workout.entity.WorkoutFeedbackMediaType;

public record WorkoutFeedbackUploadUrlFileReqDto(
        String fileName,
        String contentType,
        WorkoutFeedbackMediaType mediaType
) {
}
