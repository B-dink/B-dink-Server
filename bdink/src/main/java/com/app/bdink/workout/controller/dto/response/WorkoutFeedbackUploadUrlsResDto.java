package com.app.bdink.workout.controller.dto.response;

import java.util.List;

public record WorkoutFeedbackUploadUrlsResDto(
        List<WorkoutFeedbackUploadUrlResDto> uploadUrls
) {
}
