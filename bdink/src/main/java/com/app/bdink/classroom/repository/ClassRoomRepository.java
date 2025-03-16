package com.app.bdink.classroom.repository;

import com.app.bdink.classroom.domain.Career;
import com.app.bdink.classroom.entity.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    int countById(Long id);
    List<ClassRoom> findAllByInstructorCareer(Career career);

}
