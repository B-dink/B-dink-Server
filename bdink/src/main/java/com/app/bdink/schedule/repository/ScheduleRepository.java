package com.app.bdink.schedule.repository;

import com.app.bdink.schedule.entity.Schedule;
import com.app.bdink.schedule.domain.ScheduleType;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByScheduleType(ScheduleType scheduleType);

    List<Schedule> findByDate(LocalDate date);

}
