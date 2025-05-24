package com.app.bdink.version.controller.dto;

public record CheckUpdateRequiredResponse(
        Boolean isUpdateRequired,
        String currentVersion
) {
}
