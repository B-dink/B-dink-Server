package com.app.bdink.notification.service;

import com.app.bdink.notification.controller.dto.NotificationResponse;
import com.app.bdink.notification.entity.Notification;
import com.app.bdink.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public Long create(Notification notification) {
        return notificationRepository.save(notification).getId();
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(Long receiverMemberId) {
        return notificationRepository.findAllByReceiverMemberIdOrderByCreatedAtDesc(receiverMemberId).stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @Transactional
    public void markAsRead(Long receiverMemberId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        if (!notification.getReceiverMemberId().equals(receiverMemberId)) {
            throw new IllegalArgumentException("Invalid notification access");
        }
        notification.updateRead(true);
    }
}
