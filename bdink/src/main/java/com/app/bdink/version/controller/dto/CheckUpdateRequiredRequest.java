package com.app.bdink.version.controller.dto;

import com.app.bdink.version.entity.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CheckUpdateRequiredRequest(

        @NotBlank(message = "현재 버전은 필수입니다")
        @Pattern(regexp = "^\\d+\\.\\d+\\.\\d+$", message = "버전은 'x.y.z' 형식이어야 합니다 (예: 1.2.3)")
        @Schema(description = "앱 버전", example = "1.0.0")
        String version,

        @NotNull(message = "플랫폼은 필수입니다")
        Platform platform
) {
}
