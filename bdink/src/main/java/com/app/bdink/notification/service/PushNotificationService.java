package com.app.bdink.notification.service;

import com.app.bdink.notification.entity.DeviceToken;
import com.app.bdink.notification.repository.DeviceTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationService {

    private final DeviceTokenRepository deviceTokenRepository;

    public void sendToMember(Long memberId, String title, String body, String linkType, String linkId) {
        List<DeviceToken> tokens = deviceTokenRepository.findAllByMemberIdAndIsActiveTrueAndIsAllowedTrue(memberId);
        for (DeviceToken token : tokens) {
            sendToToken(token, title, body, linkType, linkId);
        }
    }

    private void sendToToken(DeviceToken deviceToken, String title, String body, String linkType, String linkId) {
        Message message = Message.builder()
                .setToken(deviceToken.getToken())
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putData("linkType", linkType)
                .putData("linkId", linkId)
                .build();
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            MessagingErrorCode errorCode = e.getMessagingErrorCode();
            if (errorCode == MessagingErrorCode.UNREGISTERED) {
                deviceToken.deactivate();
            }
            String httpStatus = e.getHttpResponse() != null ? String.valueOf(e.getHttpResponse().getStatusCode()) : "n/a";
            String httpContent = e.getHttpResponse() != null ? e.getHttpResponse().getContent() : "n/a";
            log.warn("FCM send failed: code={}, httpStatus={}, httpContent={}, message={}",
                    errorCode, httpStatus, httpContent, e.getMessage());
        }
    }
}
