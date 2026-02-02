package com.app.bdink.notification.controller.dto;

import com.app.bdink.notification.entity.Notification;
import com.app.bdink.notification.entity.NotificationLinkType;
import com.app.bdink.notification.entity.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        NotificationType type,
        String title,
        String body,
        NotificationLinkType linkType,
        Long linkId,
        Boolean isRead,
        LocalDateTime createdAt
) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getTitle(),
                notification.getBody(),
                notification.getLinkType(),
                notification.getLinkId(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }
}
