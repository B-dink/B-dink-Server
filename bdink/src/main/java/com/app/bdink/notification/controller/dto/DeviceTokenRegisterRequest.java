package com.app.bdink.notification.controller.dto;

import com.app.bdink.notification.entity.DevicePlatform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeviceTokenRegisterRequest(
        @NotBlank
        String token,
        @NotNull
        DevicePlatform platform,
        Boolean isAllowed
) {
}
