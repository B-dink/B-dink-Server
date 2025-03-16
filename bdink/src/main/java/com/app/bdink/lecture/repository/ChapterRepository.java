package com.app.bdink.lecture.repository;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.lecture.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    boolean existsByClassRoom(ClassRoom classRoom);
    int countByClassRoom(ClassRoom classRoom);
}
