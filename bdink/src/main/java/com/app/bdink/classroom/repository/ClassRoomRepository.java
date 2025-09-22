package com.app.bdink.classroom.repository;

import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassRoomRepository extends JpaRepository<ClassRoomEntity, Long> {
    int countById(Long id);

    int countByInstructor(Instructor instructor);

    Optional<ClassRoomEntity> findByQrToken(String qrToken);

    List<ClassRoomEntity> findAllByInstructorCareer(Career career);

    List<ClassRoomEntity> findAllByCareer(Career career);

    List<ClassRoomEntity> findAllByInstructor(Instructor instructor);
    
    // promotionOf = '23'인 classRoom을 promotion으로 임시로 지정
    List<ClassRoomEntity> findAllByPromotionOf(int promotionOf);

}
