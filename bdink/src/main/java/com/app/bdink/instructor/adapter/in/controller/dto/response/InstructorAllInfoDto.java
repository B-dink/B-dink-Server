package com.app.bdink.instructor.adapter.in.controller.dto.response;

import com.app.bdink.classroom.domain.Career;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;

public record InstructorAllInfoDto(
        Long instructorId,
        String instructorName,
        Career career,
        Integer LectureCount
) {
    public static InstructorAllInfoDto from(final Instructor instructor, final int clasRoomCount) {
        return new InstructorAllInfoDto(
                instructor.getId(),
                instructor.getMember().getName(),
                instructor.getCareer(),
                clasRoomCount
        );
    }
}
