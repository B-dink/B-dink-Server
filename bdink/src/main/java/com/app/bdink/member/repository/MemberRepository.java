package com.app.bdink.member.repository;

import com.app.bdink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);

    Optional<Member> findByKakaoId(Long id);

    Optional<Member> findByAppleId(String id);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phone);

    Optional<Member> findByRefreshToken(String refreshToken);
}
