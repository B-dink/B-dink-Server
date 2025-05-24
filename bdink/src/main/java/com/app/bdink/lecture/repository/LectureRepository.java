package com.app.bdink.lecture.repository;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.lecture.entity.Lecture;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    int countByClassRoom(ClassRoomEntity classRoomEntity);

    List<Lecture> findAllByClassRoom(ClassRoomEntity classRoomEntity);
}