package com.app.bdink.version.controller.dto;

import com.app.bdink.version.entity.Platform;
import com.app.bdink.version.entity.Version;
import lombok.Builder;

@Builder
public record UpdateCheckResponse(
        String minVersion,
        String latestVersion,
        Boolean forceUpdateRequired,
        String releaseNotes,
        Platform platform
) {
    public static UpdateCheckResponse from(Version latestVersion,
                                           String minVersion,
                                           boolean forceUpdateRequired) {
        return UpdateCheckResponse.builder()
                .minVersion(minVersion)
                .latestVersion(latestVersion.getVersion())
                .forceUpdateRequired(forceUpdateRequired)
                .releaseNotes(latestVersion.getReleaseNotes())
                .platform(latestVersion.getPlatform())
                .build();
    }
}
