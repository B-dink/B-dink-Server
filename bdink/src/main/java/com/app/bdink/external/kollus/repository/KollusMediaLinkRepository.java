package com.app.bdink.external.kollus.repository;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.external.kollus.entity.KollusMedia;
import com.app.bdink.external.kollus.entity.KollusMediaLink;
import com.app.bdink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KollusMediaLinkRepository extends JpaRepository<KollusMediaLink, Long> {

    // 특정 사용자가 어떤 미디어 콘텐츠의 accessToken을 요청
    Optional<KollusMediaLink> findByMemberIdAndKollusMediaId(Long memberId, Long kollusMediaId);

    Optional<KollusMediaLink> findByMemberIdAndLectureId(Long memberId, Long lectureId);

    int countByMemberAndLectureClassRoomAndCompleted(Member member, ClassRoomEntity classRoomEntity, boolean completed);

    List<KollusMediaLink> findAllByMemberIdAndLectureIdIn(Long memberId, List<Long> lectureIds);

    // 사용자별 등록된 영상 리스트 가져오기
    List<KollusMediaLink> findAllByMemberId(Long memberId);

    boolean existsByMemberAndKollusMedia(Member member, KollusMedia kollusMedia);

    int countByMemberAndLecture_ClassRoomAndPlaytimePercentGreaterThanEqual(Member member,
                                                                                 ClassRoomEntity classRoomEntity,
                                                                                 int progressPercent);

    List<KollusMediaLink> findAllByMemberAndLectureClassRoom(Member member, ClassRoomEntity classRoomEntity);
}
