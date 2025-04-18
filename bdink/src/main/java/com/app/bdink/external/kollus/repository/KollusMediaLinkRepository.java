package com.app.bdink.external.kollus.repository;

import com.app.bdink.external.kollus.entity.KollusMediaLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KollusMediaLinkRepository extends JpaRepository<KollusMediaLink, Long> {
    // 특정 사용자가 어떤 미디어 콘텐츠의 accessToken을 요청
    Optional<KollusMediaLink> findByMemberIdAndMediaContentKey(Long memberId, String mediaContentKey);

    // 사용자별 등록된 영상 리스트 가져오기
    List<KollusMediaLink> findAllByMemberId(Long memberId);
}
