package com.app.bdink.version.controller.dto;

import com.app.bdink.version.entity.Platform;
import com.app.bdink.version.entity.Version;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VersionRequest(

        @NotBlank(message = "현재 버전은 필수입니다")
        @Pattern(regexp = "^\\d+\\.\\d+\\.\\d+$", message = "버전은 'x.y.z' 형식이어야 합니다 (예: 1.2.3)")
        @Schema(description = "앱 버전", example = "1.0.0")
        String version,

        @NotNull(message = "해당 버전이 필수인지 여부를 기입해야 합니다.")
        @Schema(description = "강제 업데이트 필요 여부", example = "true")
        Boolean forceUpdateRequired,

        @Schema(description = "릴리즈 노트", example = "버그 수정 및 성능 개선")
        String releaseNotes,

        @NotNull(message = "플랫폼은 필수입니다")
        @Schema(description = "플랫폼", example = "iOS")
        Platform platform
) {
        public Version toEntity() {
                return Version.builder()
                        .version(this.version)
                        .forceUpdateRequired(this.forceUpdateRequired)
                        .releaseNotes(this.releaseNotes)
                        .platform(this.platform)
                        .build();
        }
}