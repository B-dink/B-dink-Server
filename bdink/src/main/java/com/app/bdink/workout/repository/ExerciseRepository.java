package com.app.bdink.workout.repository;

import com.app.bdink.workout.controller.dto.ExercisePart;
import com.app.bdink.workout.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    // 부위별 운동 종목 조회
    List<Exercise> findAllByPart(ExercisePart part);

    // part + keyword의 조합
    List<Exercise> findByPartAndNameContainingIgnoreCase(ExercisePart part, String name);

    @Query("select max(e.updatedAt) from Exercise e")
    Optional<LocalDateTime> findLastUpdatedAt();

    @Query("select max(e.createdAt) from Exercise e")
    Optional<LocalDateTime> findLastCreatedAt();

    default Optional<LocalDate> findLastCreatedAtDate() {
        return findLastCreatedAt().map(LocalDateTime::toLocalDate);
    }

    @Query("""
       select e
       from Exercise e
       where e.createdAt >= :start
         and e.createdAt < :end
       """)
    List<Exercise> findAllByCreatedAtBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // ai 메모장 mvp
    List<Exercise> findByNameContainingIgnoreCase(String keyword);
}
