package com.app.bdink.notification.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 수신자 멤버 ID (알림 대상)
    @Column(nullable = false)
    private Long receiverMemberId;

    // 알림 타입 (도메인 이벤트 구분)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    // 알림 제목
    @Column(nullable = false)
    private String title;

    // 알림 본문
    @Column(nullable = false)
    private String body;

    // 딥링크 타입 (앱 내 이동 대상)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationLinkType linkType;

    // 딥링크 대상 ID (예: sessionId, questionId 등)
    @Column
    private Long linkId;

    // 읽음 여부
    @Column(nullable = false)
    private Boolean isRead;

    // 삭제 여부 (soft delete)
    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    public Notification(Long receiverMemberId,
                        NotificationType type,
                        String title,
                        String body,
                        NotificationLinkType linkType,
                        Long linkId) {
        this.receiverMemberId = receiverMemberId;
        this.type = type;
        this.title = title;
        this.body = body;
        this.linkType = linkType;
        this.linkId = linkId;
        this.isRead = false;
        this.isDeleted = false;
    }

    public void updateRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}
