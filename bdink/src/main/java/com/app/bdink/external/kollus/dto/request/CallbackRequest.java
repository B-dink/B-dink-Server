package com.app.bdink.external.kollus.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@RequiredArgsConstructor
public class CallbackRequest {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "KOLLUS_REQ_01 : 업로드 콜백 DTO")
    public static class uploadRequestDTO {
        String content_provider_key;        // 고객사의 서비스 계정 키
        String filename;                    // 업로드 된 파일명
        String upload_file_key;             // 업로드 파일 키
        String media_content_key;           // 미디어 컨텐츠 키
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "KOLLUS_REQ_02 : 삭제 콜백 DTO")
    public static class deleteRequestDTO {
        String content_provider_key; // 고객사의 서비스 계정 키
        String full_filename;       // 폴더를 포함한 업로드 된 파일명
        String filename;            // 업로드 된 파일명
        String upload_file_key;     // 업로드 파일 키
        String media_content_key;   // 미디어 컨텐츠 키
        String channel_key;         // 콘텐츠가 할당된 채널 키
        String channel_name;        // 콘텐츠가 할당된 채널의 이름
        String update_type;         // 업데이트 종류
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "KOLLUS_REQ_03 : 플레이 콜백 DTO")
    public static class playRequestDTO {
        String content_provider_key;
        String media_content_key;
        String media_profile_key;
        String user_key;
        String log_type;
        String full_filename;
        String play_time;
        String duration;
    }
}
