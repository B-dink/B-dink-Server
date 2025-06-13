package com.app.bdink.classroom.repository;

import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassRoomRepository extends JpaRepository<ClassRoomEntity, Long> {
    int countById(Long id);

    int countByInstructor(Instructor instructor);

    List<ClassRoomEntity> findAllByInstructorCareer(Career career);

    List<ClassRoomEntity> findAllByCareer(Career career);

    List<ClassRoomEntity> findAllByInstructor(Instructor instructor);

}
