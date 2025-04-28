package com.app.bdink.external.kollus.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "KOLLUS_REQ_04 : 유저키 생성 요청 DTO")
public record UserKeyDTO (
        String userKey
){ }
