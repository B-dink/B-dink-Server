package com.app.bdink.sugang.repository;

import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.member.entity.Member;
import com.app.bdink.sugang.entity.Sugang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SugangRepository extends JpaRepository<Sugang, Long> {
    int countByMemberAndLectureAndCompleted(Member member, Lecture lecture, boolean completed);
}
