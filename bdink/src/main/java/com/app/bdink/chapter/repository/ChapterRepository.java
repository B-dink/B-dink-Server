package com.app.bdink.chapter.repository;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.chapter.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    boolean existsByClassRoom(ClassRoomEntity classRoomEntity);
    int countByClassRoom(ClassRoomEntity classRoomEntity);

    @Query("SELECT c FROM Chapter c JOIN FETCH c.classRoom WHERE c.id = :id")
    Optional<Chapter> findWithClassRoomById(@Param("id") Long id);
}
