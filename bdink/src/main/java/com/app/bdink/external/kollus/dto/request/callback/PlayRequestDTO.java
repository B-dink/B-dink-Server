package com.app.bdink.external.kollus.dto.request.callback;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "KOLLUS_REQ_03 : Kollus play callback DTO")
public record PlayRequestDTO (
        String media_content_key,
        String play_time,
        String duration,
        String playtime_percent,
        String client_user_id
)
{ }
