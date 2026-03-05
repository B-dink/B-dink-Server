package com.app.bdink.workout.controller.dto.request;

import java.util.List;

public record WorkoutFeedbackSaveReqDto(
        String content,
        List<WorkoutFeedbackMediaReqDto> media
) {
}
