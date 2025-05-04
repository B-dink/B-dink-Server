package com.app.bdink.classroom.adapter.in.controller.dto.request;

import java.util.List;

public record ClassRoomImageCreateRequest(
        List<String> imageUrls
) {}