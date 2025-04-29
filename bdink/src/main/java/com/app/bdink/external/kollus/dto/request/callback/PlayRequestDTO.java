package com.app.bdink.external.kollus.dto.request.callback;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "KOLLUS_REQ_03 : Kollus play callback DTO")
public record PlayRequestDTO (
        String content_provider_key,
        String media_content_key,
        String media_profile_key,
        String user_key,
        String log_type,
        String full_filename,
        String play_time,
        String duration
)
{ }
