package com.app.bdink.classroom.adapter.in.controller.dto.response;

public record ClassRoomProgressResponse(
        String title,
        String instructor,
        String status // 완강, --%
        ) {
}
