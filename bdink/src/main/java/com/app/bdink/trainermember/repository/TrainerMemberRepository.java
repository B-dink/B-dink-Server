package com.app.bdink.trainermember.repository;

import com.app.bdink.trainermember.entity.TrainerMember;
import com.app.bdink.trainermember.entity.TrainerMemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 트레이너-멤버 소속 조회/저장을 위한 JPA Repository.
 * 활성 상태 조회 및 멤버 단일 소속 조회를 제공한다.
 */
public interface TrainerMemberRepository extends JpaRepository<TrainerMember, Long> {
    Optional<TrainerMember> findByIdAndStatus(Long id, TrainerMemberStatus status);

    Optional<TrainerMember> findByMemberIdAndStatus(Long memberId, TrainerMemberStatus status);

    Optional<TrainerMember> findByMemberId(Long memberId);

    boolean existsByMemberIdAndStatus(Long memberId, TrainerMemberStatus status);

    List<TrainerMember> findAllByTrainerIdAndStatus(Long trainerId, TrainerMemberStatus status);
}
