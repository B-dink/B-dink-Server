package com.app.bdink.notification.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.notification.entity.DevicePlatform;
import com.app.bdink.notification.entity.DeviceToken;
import com.app.bdink.notification.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;

    @Transactional
    public void registerOrUpdate(Long memberId, String token, DevicePlatform platform, Boolean isAllowed) {
        DeviceToken existing = deviceTokenRepository.findByToken(token).orElse(null);
        if (existing != null) {
            if (!existing.getMemberId().equals(memberId)) {
                throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
            }
            existing.updateToken(token, platform, isAllowed);
            return;
        }
        deviceTokenRepository.save(DeviceToken.builder()
                .memberId(memberId)
                .token(token)
                .platform(platform)
                .isAllowed(isAllowed)
                .build());
    }

    @Transactional
    public void updateAllowed(Long memberId, String token, Boolean isAllowed) {
        DeviceToken deviceToken = deviceTokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND, Error.NOT_FOUND.getMessage()));
        if (!deviceToken.getMemberId().equals(memberId)) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }
        deviceToken.updateAllowed(isAllowed);
    }

    @Transactional
    public void deactivate(Long memberId, String token) {
        DeviceToken deviceToken = deviceTokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND, Error.NOT_FOUND.getMessage()));
        if (!deviceToken.getMemberId().equals(memberId)) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }
        deviceToken.deactivate();
    }

    @Transactional
    public void deactivateAllForMember(Long memberId) {
        for (DeviceToken deviceToken : deviceTokenRepository.findAllByMemberIdAndIsActiveTrue(memberId)) {
            deviceToken.deactivate();
        }
    }
}
