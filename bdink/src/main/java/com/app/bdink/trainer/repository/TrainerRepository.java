package com.app.bdink.trainer.repository;

import com.app.bdink.trainer.entity.Trainer;
import com.app.bdink.trainer.entity.TrainerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 트레이너 조회/저장을 위한 JPA Repository.
 * 활성 상태 조회를 기본으로 사용하는 메서드를 제공한다.
 */
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByIdAndStatus(Long id, TrainerStatus status);

    Optional<Trainer> findByMemberIdAndStatus(Long memberId, TrainerStatus status);

    Optional<Trainer> findByMemberId(Long memberId);

    boolean existsByMemberIdAndStatus(Long memberId, TrainerStatus status);

    List<Trainer> findAllByCenterIdAndStatus(Long centerId, TrainerStatus status);

    List<Trainer> findAllByQrTokenAndStatus(String qrToken, TrainerStatus status);
}
