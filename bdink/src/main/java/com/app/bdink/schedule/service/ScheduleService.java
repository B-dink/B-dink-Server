package com.app.bdink.schedule.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.entity.Role;
import com.app.bdink.schedule.controller.dto.request.ScheduleRequest;
import com.app.bdink.schedule.controller.dto.response.ScheduleResponse;
import com.app.bdink.schedule.domain.CompetitionType;
import com.app.bdink.schedule.entity.Schedule;
import com.app.bdink.schedule.domain.ScheduleType;
import com.app.bdink.schedule.repository.ScheduleRepository;

import java.time.LocalDate;
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
    public String createSchedule(final Member member, final ScheduleRequest scheduleRequest, String key) {
        validateAdmin(member);
        Schedule schedule = scheduleRequest.toEntity();
        schedule.updateImage(key);
        Long id = scheduleRepository.save(schedule)
            .getId();
        return String.valueOf(id);
    }

    public List<ScheduleResponse> getAllSchedule() {
        List<Schedule> scheduleList = scheduleRepository.findAll();
        return scheduleList.stream()
            .map(ScheduleResponse::new)
            .collect(Collectors.toList());
    }

    public List<ScheduleResponse> getScheduleByDate(String localDate){
        return scheduleRepository.findByDate(LocalDate.parse(localDate)).stream()
                .map(ScheduleResponse::new)
                .toList();
    }

    public List<ScheduleResponse> getScheduleByScheduleType(String scheduleType) {
        List<Schedule> scheduleList = scheduleRepository.findByScheduleType(ScheduleType.valueOf(scheduleType.toUpperCase()));
        return scheduleList.stream()
            .map(ScheduleResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateSchedule(final Member member, Long scheduleId, final ScheduleRequest scheduleRequest, String image) {
        validateAdmin(member);
        Schedule schedule = getById(scheduleId);

        schedule.update(scheduleRequest.title(), scheduleRequest.date(), scheduleRequest.scheduleType().toUpperCase(), image, CompetitionType.valueOf(scheduleRequest.competitionType()));
    }

    @Transactional
    public void deleteSchedule(final Member member, Long scheduleId) {
        validateAdmin(member);
        Schedule schedule = getById(scheduleId);

        scheduleRepository.delete(schedule);
    }

    public Schedule getById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(
            () -> new CustomException(Error.NOT_FOUND_SCHEDULE, Error.NOT_FOUND_SCHEDULE.getMessage())
        );
    }

    public void validateAdmin(final Member member) {
        if (!member.getRole().equals(Role.valueOf("ROLE_ADMIN"))) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }
    }
}
