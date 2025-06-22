package com.app.bdink.member.repository;

import com.app.bdink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);

    Optional<Member> findByKakaoId(Long id);

    Optional<Member> findByAppleId(String id);

    Optional<Member> findByPhoneNumber(String phone);

    Optional<Member> findByKollusClientUserId(String kollusClientUserId);

    boolean existsByName(String name);

    boolean existsByPhoneNumber(String phone);

    Optional<Member> findByRefreshToken(String refreshToken);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.createdAt >= :start AND m.createdAt < :end AND m.role != com.app.bdink.member.entity.Role.DELETE_USER")
    long countDailySignup(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.role != com.app.bdink.member.entity.Role.DELETE_USER")
    long countTotalMember();
}
