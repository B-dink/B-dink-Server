package com.app.bdink.version.controller.dto;

import com.app.bdink.version.entity.Platform;

public record GetLatestVersionRequest(
        Platform platform
) {
}
