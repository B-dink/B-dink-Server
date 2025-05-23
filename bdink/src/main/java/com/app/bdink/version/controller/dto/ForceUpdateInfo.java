package com.app.bdink.version.controller.dto;

public record ForceUpdateInfo(
        Boolean isForceUpdateRequired,
        String forceUpdateVersion) {
}
