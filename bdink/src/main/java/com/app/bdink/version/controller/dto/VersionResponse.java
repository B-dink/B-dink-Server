package com.app.bdink.version.controller.dto;

import com.app.bdink.version.entity.Platform;
import com.app.bdink.version.entity.Version;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class VersionResponse {
    private Long id;
    private String currentVersion;
    private String minimumRequiredVersion;
    private Boolean forceUpdateRequired;
    private String releaseNotes;
    private LocalDateTime releaseDate;
    private Platform platform;

    public static VersionResponse from(Version version) {
        return VersionResponse.builder()
                .id(version.getId())
                .currentVersion(version.getCurrentVersion())
                .minimumRequiredVersion(version.getMinimumRequiredVersion())
                .forceUpdateRequired(version.getForceUpdateRequired())
                .releaseNotes(version.getReleaseNotes())
                .releaseDate(version.getReleaseDate())
                .platform(version.getPlatform())
                .build();
    }
}
