package com.app.bdink.sugang.repository;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.member.entity.Member;
import com.app.bdink.sugang.controller.dto.SugangStatus;
import com.app.bdink.sugang.entity.Sugang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SugangRepository extends JpaRepository<Sugang, Long> {
    List<Sugang> findAllByMember(Member member);

    List<Sugang> findByExpiredDateBeforeAndSugangStatus(LocalDate now, SugangStatus status);

    Optional<Sugang> findByMemberAndClassRoomEntity(Member member, ClassRoomEntity classRoomEntity);
    
    List<Sugang> findByMemberAndSugangStatus(Member member, SugangStatus sugangStatus);
}
