package com.app.bdink.lecture.repository;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    int countByClassRoom(ClassRoomEntity classRoomEntity);

    List<Lecture> findAllByClassRoom(ClassRoomEntity classRoomEntity);

    //LocalDate의 합 값을 사용하기 위한 메서드
    @Query(value = """
                SELECT SUM(TIME_TO_SEC(time))
                FROM Lecture
                WHERE classRoom_id = :classRoomId
            """, nativeQuery = true)
    Long getTotalLectureSeconds(@Param("classRoomId") Long classRoomId);
}