package com.app.bdink.version.controller.dto;

import com.app.bdink.version.entity.Platform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record VersionRequest(

        @NotBlank(message = "현재 버전은 필수입니다")
        @Pattern(regexp = "^\\d+\\.\\d+\\.\\d+$", message = "버전은 'x.y.z' 형식이어야 합니다 (예: 1.2.3)")
        String currentVersion,

        @NotBlank(message = "해당 버전이 필수인지 여부를 기입해야 합니다.")
        Boolean minimumRequiredVersion,

        Boolean forceUpdateRequired,

        String releaseNotes,

        LocalDateTime releaseDate,

        @NotNull(message = "플랫폼은 필수입니다")
        Platform platform
) {
}