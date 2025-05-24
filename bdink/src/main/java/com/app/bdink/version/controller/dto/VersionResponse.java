package com.app.bdink.version.controller.dto;

import com.app.bdink.version.entity.Platform;
import com.app.bdink.version.entity.Version;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record VersionResponse(
        Long versionId,
        String version,
        Boolean forceUpdateRequired,
        String releaseNotes,
        Platform platform
) {

    public static VersionResponse from(Version version) {
        return VersionResponse.builder()
                .versionId(version.getId())
                .version(version.getVersion())
                .forceUpdateRequired(version.getForceUpdateRequired())
                .releaseNotes(version.getReleaseNotes())
                .platform(version.getPlatform())
                .build();
    }
}
