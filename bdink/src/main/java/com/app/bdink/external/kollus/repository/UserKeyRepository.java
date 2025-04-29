package com.app.bdink.external.kollus.repository;

import com.app.bdink.external.kollus.entity.UserKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserKeyRepository extends JpaRepository<UserKey, Long> {
    @Override
    Optional<UserKey> findById(Long id);

    Optional<UserKey> findFirstByMemberIdAndIsRevokedFalseOrderByAssignedAtDesc(Long memberId);

    Optional<UserKey> findFirstByMemberIsNull();
}
