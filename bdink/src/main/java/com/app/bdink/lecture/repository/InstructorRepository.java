package com.app.bdink.lecture.repository;

import com.app.bdink.classroom.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}
