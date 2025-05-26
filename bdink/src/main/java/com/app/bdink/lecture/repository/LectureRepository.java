package com.app.bdink.lecture.repository;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    int countByClassRoom(ClassRoomEntity classRoomEntity);

    List<Lecture> findAllByClassRoom(ClassRoomEntity classRoomEntity);
}