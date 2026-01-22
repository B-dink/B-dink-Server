package com.app.bdink.notification.repository;

import com.app.bdink.notification.entity.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    Optional<DeviceToken> findByToken(String token);
    List<DeviceToken> findAllByMemberIdAndIsActiveTrueAndIsAllowedTrue(Long memberId);
    List<DeviceToken> findAllByMemberIdAndIsActiveTrue(Long memberId);
}
