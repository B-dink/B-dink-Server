package com.app.bdink.instructor.adapter.in.controller.dto;

import com.app.bdink.classroom.domain.Career;

public record InstructorDto(
    String career,
    String marketingText,

    String marketingSites
) {
    public Career toCareer(){
        return Career.valueOf(this.career);
    }
}
