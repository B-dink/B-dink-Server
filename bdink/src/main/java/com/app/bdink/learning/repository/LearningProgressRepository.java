package com.app.bdink.learning.repository;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.learning.entity.LearningProgress;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LearningProgressRepository extends JpaRepository<LearningProgress, Long> {

    Optional<LearningProgress> findByMemberAndLecture(Member member, Lecture lecture);

    List<LearningProgress> findAllByMemberAndLectureClassRoom(Member member, ClassRoomEntity classRoomEntity);

    int countByMemberAndLectureClassRoomAndCompleted(Member member, ClassRoomEntity classRoomEntity, boolean completed);
}
