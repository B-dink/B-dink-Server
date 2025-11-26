package com.app.bdink.workout.repository;

import com.app.bdink.member.entity.Member;
import com.app.bdink.workout.controller.dto.MemberWeeklyVolumeDto;
import com.app.bdink.workout.entity.PerformedExercise;
import com.app.bdink.workout.entity.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long> {
    List<WorkoutSet> findByPerformedExercise(PerformedExercise performedExercise);

    void deleteByPerformedExercise(PerformedExercise performedExercise);

    @Query("""
            select new com.app.bdink.workout.controller.dto.MemberWeeklyVolumeDto(
                s.member.id,
                sum(ws.weight * ws.reps)
            )
            from WorkoutSet ws
            join ws.performedExercise pe
            join pe.workOutSession s
            where s.createdAt between :start and :end
            group by s.member.id
            order by sum(ws.weight * ws.reps) desc
            """)
    List<MemberWeeklyVolumeDto> findWeeklyVolumeRanking(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    /**
     * 특정 회원의 특정 기간 볼륨 합계 (없으면 null → 0 처리)
     */
    @Query("""
            select coalesce(sum(ws.weight * ws.reps), 0)
            from WorkoutSet ws
            join ws.performedExercise pe
            join pe.workOutSession s
            where s.member = :member
              and s.createdAt between :start and :end
            """)
    Long calculateVolumeForPeriod(
            @Param("member") Member member,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
