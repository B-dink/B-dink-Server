package com.app.bdink.lecture.repository;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.lecture.entity.Lecture;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    int countByClassRoom(ClassRoomEntity classRoomEntity);

    List<Lecture> findAllByClassRoom(ClassRoomEntity classRoomEntity);
}