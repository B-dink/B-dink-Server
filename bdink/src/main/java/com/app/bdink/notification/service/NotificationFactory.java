package com.app.bdink.notification.service;

import com.app.bdink.notification.entity.Notification;
import com.app.bdink.notification.entity.NotificationLinkType;
import com.app.bdink.notification.entity.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class NotificationFactory {

    public Notification create(Long receiverMemberId,
                               NotificationType type,
                               String title,
                               String body,
                               NotificationLinkType linkType,
                               Long linkId) {
        return Notification.builder()
                .receiverMemberId(receiverMemberId)
                .type(type)
                .title(title)
                .body(body)
                .linkType(linkType)
                .linkId(linkId)
                .build();
    }
}
