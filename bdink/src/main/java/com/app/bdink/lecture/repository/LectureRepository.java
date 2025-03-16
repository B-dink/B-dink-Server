package com.app.bdink.lecture.repository;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    int countByClassRoom(ClassRoom classRoom);
}