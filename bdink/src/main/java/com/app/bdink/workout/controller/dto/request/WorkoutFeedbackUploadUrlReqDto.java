package com.app.bdink.workout.controller.dto.request;

import java.util.List;

public record WorkoutFeedbackUploadUrlReqDto(
        List<WorkoutFeedbackUploadUrlFileReqDto> files
) {
}
