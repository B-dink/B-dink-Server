package com.app.bdink.version.controller.dto;

import lombok.Builder;

@Builder
public record UpdateCheckResponse(
        Boolean isUpdateRequired,
        Boolean isForceUpdateRequired,
        String currentVersion,
        String minimumRequiredVersion
) {
    public static UpdateCheckResponse from(CheckUpdateRequiredResponse checkResponse,
                                           ForceUpdateInfo forceUpdateInfo) {
        return UpdateCheckResponse.builder()
                .isUpdateRequired(checkResponse.isUpdateRequired())
                .isForceUpdateRequired(forceUpdateInfo.isForceUpdateRequired())
                .currentVersion(checkResponse.currentVersion())
                .minimumRequiredVersion(forceUpdateInfo.forceUpdateVersion())
                .build();
    }
}
