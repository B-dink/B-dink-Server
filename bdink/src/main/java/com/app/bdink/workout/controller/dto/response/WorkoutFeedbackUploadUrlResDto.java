package com.app.bdink.workout.controller.dto.response;

import com.app.bdink.workout.entity.WorkoutFeedbackMediaType;

public record WorkoutFeedbackUploadUrlResDto(
        WorkoutFeedbackMediaType mediaType,
        String fileName,
        String s3Key,
        String presignedUrl
) {
}
