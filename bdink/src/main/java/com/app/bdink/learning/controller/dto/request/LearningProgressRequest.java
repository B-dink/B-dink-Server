package com.app.bdink.learning.controller.dto.request;

import com.app.bdink.learning.entity.LearningEventType;

import java.time.LocalDateTime;

public record LearningProgressRequest(
        int positionSec,
        int durationSec,
        LearningEventType eventType,
        String sessionId,
        LocalDateTime clientOccurredAt
) {
}
