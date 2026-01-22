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

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 토큰 소유 멤버 ID
    @Column(nullable = false)
    private Long memberId;

    // FCM 디바이스 토큰
    @Column(nullable = false, unique = true)
    private String token;

    // 플랫폼 구분
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DevicePlatform platform;

    // 토큰 활성 여부 (유효하지 않으면 false)
    @Column(nullable = false)
    private Boolean isActive;

    // 알림 권한 허용 여부
    @Column(nullable = false)
    private Boolean isAllowed;

    // 마지막 접속/토큰 갱신 시각
    private LocalDateTime lastSeenAt;

    @Builder
    public DeviceToken(Long memberId,
                       String token,
                       DevicePlatform platform,
                       Boolean isAllowed) {
        this.memberId = memberId;
        this.token = token;
        this.platform = platform;
        this.isActive = true;
        this.isAllowed = isAllowed != null ? isAllowed : true;
        this.lastSeenAt = LocalDateTime.now();
    }

    public void updateToken(String token, DevicePlatform platform, Boolean isAllowed) {
        this.token = token;
        this.platform = platform;
        if (isAllowed != null) {
            this.isAllowed = isAllowed;
        }
        this.isActive = true;
        this.lastSeenAt = LocalDateTime.now();
    }

    public void updateAllowed(Boolean isAllowed) {
        this.isAllowed = isAllowed;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void updateLastSeenAt(LocalDateTime lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }
}
