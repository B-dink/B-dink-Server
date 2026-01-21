package com.app.bdink.notification.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeviceTokenAllowedRequest(
        @NotBlank
        String token,
        @NotNull
        Boolean isAllowed
) {
}
