package com.app.bdink.learning.controller.dto.response;

import java.time.LocalDateTime;

public record LearningProgressResponse(
        Long lectureId,
        int lastPositionSec,
        int maxPositionSec,
        int durationSec,
        double progressPercent,
        boolean completed,
        LocalDateTime lastWatchedAt
) {
}
