package com.app.bdink.lecture.repository;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.lecture.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    boolean existsByClassRoom(ClassRoomEntity classRoomEntity);
    int countByClassRoom(ClassRoomEntity classRoomEntity);
}
