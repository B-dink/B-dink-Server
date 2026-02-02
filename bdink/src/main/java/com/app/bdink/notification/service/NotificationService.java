package com.app.bdink.notification.service;

import com.app.bdink.notification.controller.dto.NotificationResponse;
import com.app.bdink.notification.entity.Notification;
import com.app.bdink.notification.repository.NotificationRepository;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PushNotificationService pushNotificationService;

    @Transactional
    public Long create(Notification notification) {
        Notification saved = notificationRepository.save(notification);
        pushNotificationService.sendToMember(
                saved.getReceiverMemberId(),
                saved.getTitle(),
                saved.getBody(),
                saved.getLinkType().name(),
                saved.getLinkId() == null ? "" : saved.getLinkId().toString()
        );
        return saved.getId();
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(Long receiverMemberId) {
        return notificationRepository.findAllByReceiverMemberIdAndIsDeletedFalseOrderByCreatedAtDesc(receiverMemberId).stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @Transactional
    public void markAsRead(Long receiverMemberId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_NOTIFICATION, Error.NOT_FOUND_NOTIFICATION.getMessage()));
        if (!notification.getReceiverMemberId().equals(receiverMemberId)) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }
        notification.updateRead(true);
    }

    @Transactional
    public boolean hasUnread(Long memberId) {
        return notificationRepository.existsByReceiverMemberIdAndIsReadFalseAndIsDeletedFalse(memberId);
    }

    @Transactional
    public void softDeleteNotification(Long receiverMemberId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_NOTIFICATION, Error.NOT_FOUND_NOTIFICATION.getMessage()));
        if (!notification.getReceiverMemberId().equals(receiverMemberId)) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }
        notification.softDelete();
    }
}
