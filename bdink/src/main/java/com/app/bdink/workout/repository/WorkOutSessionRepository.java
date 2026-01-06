package com.app.bdink.workout.repository;

import com.app.bdink.member.entity.Member;
import com.app.bdink.workout.entity.WorkOutSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WorkOutSessionRepository extends JpaRepository<WorkOutSession, Long> {
    Optional<WorkOutSession> findByIdAndMember(Long id, Member member);

    Optional<WorkOutSession> findByIdAndMemberId(Long id, Long memberId);

    List<WorkOutSession> findByMemberAndCreatedAtBetween(Member member,
                                                         LocalDateTime start,
                                                         LocalDateTime end);
}
