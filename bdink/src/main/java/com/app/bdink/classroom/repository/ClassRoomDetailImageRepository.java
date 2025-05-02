package com.app.bdink.classroom.repository;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomDetailImage;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassRoomDetailImageRepository extends JpaRepository<ClassRoomDetailImage, Long> {
    List<ClassRoomDetailImage> findByClassRoomOrderBySortOrderAsc(ClassRoomEntity classRoom);
}
