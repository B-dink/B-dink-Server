package com.app.bdink.classroom.adapter.in.controller.dto.response;

import java.util.List;

public record CategorizedClassroomDto(
        List<CareerClassroomDto> classrooms
) {
    public static CategorizedClassroomDto from(List<CareerClassroomDto> careerClassroomDtos){
        return new CategorizedClassroomDto(careerClassroomDtos);
    }
}
