package com.app.bdink.sugang.repository;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.member.entity.Member;
import com.app.bdink.sugang.entity.Sugang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SugangRepository extends JpaRepository<Sugang, Long> {
    List<Sugang> findAllByMember(Member member);

    Optional<Sugang> findByMemberAndClassRoomEntity(Member member, ClassRoomEntity classRoomEntity);
}
