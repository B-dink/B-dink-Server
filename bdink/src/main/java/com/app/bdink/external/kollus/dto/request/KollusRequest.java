package com.app.bdink.external.kollus.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@RequiredArgsConstructor
public class KollusRequest {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "KOLLUS_REQ_04 : 유저키 생성 요청 DTO")
    public static class userKeyDTO {
        String userKey;              // 사용자 ID
    }
}
