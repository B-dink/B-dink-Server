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
        String client_user_id;              // 사용자 ID
        String remote_addr;                 // IP주소
        String user_agent;                  // 접속환경
        Long user_key_timeout;           // 만료시간(초)
    }
}
