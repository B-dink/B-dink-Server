package com.app.bdink.lecture.controller.dto;

import com.app.bdink.classroom.domain.Career;

public record InstructorDto(
    String career
) {
    public Career toCareer(){
        return Career.valueOf(this.career);
    }
}
