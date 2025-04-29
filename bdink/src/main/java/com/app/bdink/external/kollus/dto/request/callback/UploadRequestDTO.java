package com.app.bdink.external.kollus.dto.request.callback;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "KOLLUS_REQ_01 : Kollus media 업로드 callback DTO")
public record UploadRequestDTO (
        String content_provider_key,        // 고객사의 서비스 계정 키
        String filename,                    // 업로드 된 파일명
        String upload_file_key,             // 업로드 파일 키
        String media_content_key,           // 미디어 컨텐츠 키
        String channel_key,                  // 채널 키
        String channel_name                 // 채널 이름
){ }