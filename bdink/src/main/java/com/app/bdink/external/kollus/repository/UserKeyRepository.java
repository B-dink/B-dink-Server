package com.app.bdink.external.kollus.repository;

import com.app.bdink.external.kollus.entity.UserKey;
import com.app.bdink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserKeyRepository extends JpaRepository<UserKey, Long> {
    @Override
    Optional<UserKey> findById(Long id);

    Optional<UserKey> findByMember(Member member);

    Optional<UserKey> findFirstByMemberIdAndIsRevokedFalseOrderByAssignedAtDesc(Long memberId);

    Optional<UserKey> findFirstByMemberIsNull();
}
