package com.app.bdink.external.kollus.dto.request.callback;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "KOLLUS_REQ_02 : Kollus media 삭제 callback DTO")
public record DeleteRequestDTO(
        String media_content_key
) {
}
