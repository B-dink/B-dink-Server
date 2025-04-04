package com.app.bdink.instructor.adapter.in.controller.dto;

import com.app.bdink.classroom.domain.Career;

public record InstructorDto(
    String career
) {
    public Career toCareer(){
        return Career.valueOf(this.career);
    }
}
