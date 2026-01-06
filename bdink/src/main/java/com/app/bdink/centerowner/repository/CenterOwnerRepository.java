package com.app.bdink.centerowner.repository;

import com.app.bdink.centerowner.entity.CenterOwner;
import com.app.bdink.centerowner.entity.CenterOwnerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 센터장 조회/저장을 위한 JPA Repository.
 * 활성 상태 및 센터-멤버 조합 조회를 제공한다.
 */
public interface CenterOwnerRepository extends JpaRepository<CenterOwner, Long> {
    Optional<CenterOwner> findByIdAndStatus(Long id, CenterOwnerStatus status);

    Optional<CenterOwner> findByCenterIdAndMemberId(Long centerId, Long memberId);

    boolean existsByCenterIdAndMemberIdAndStatus(Long centerId, Long memberId, CenterOwnerStatus status);

    List<CenterOwner> findAllByCenterIdAndStatus(Long centerId, CenterOwnerStatus status);
}
