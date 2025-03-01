package com.app.bdink.schedule.service;

import com.app.bdink.schedule.controller.dto.request.ScheduleRequest;
import com.app.bdink.schedule.controller.dto.response.ScheduleResponse;
import com.app.bdink.schedule.entity.Schedule;
import com.app.bdink.schedule.domain.ScheduleType;
import com.app.bdink.schedule.repository.ScheduleRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public String createSchedule(final ScheduleRequest scheduleRequest) {
        Long id = scheduleRepository.save(
            scheduleRequest.toEntity())
            .getId();
        return String.valueOf(id);
    }

    public List<ScheduleResponse> getAllSchedule() {
        List<Schedule> scheduleList = scheduleRepository.findAll();
        return scheduleList.stream()
            .map(ScheduleResponse::new)
            .collect(Collectors.toList());
    }

    public List<ScheduleResponse> getScheduleByScheduleType(String scheduleType) {
        List<Schedule> scheduleList = scheduleRepository.findByScheduleType(ScheduleType.valueOf(scheduleType.toUpperCase()));
        return scheduleList.stream()
            .map(ScheduleResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateSchedule(Long scheduleId, final ScheduleRequest scheduleRequest) {
        Schedule schedule = getById(scheduleId);

        schedule.update(scheduleRequest.title(), scheduleRequest.date(), scheduleRequest.scheduleType().toUpperCase());
    }

    @Transactional
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = getById(scheduleId);

        scheduleRepository.delete(schedule);
    }

    public Schedule getById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(
            () -> new IllegalArgumentException("해당 일정을 찾지 못했습니다.")
        );
    }
}
