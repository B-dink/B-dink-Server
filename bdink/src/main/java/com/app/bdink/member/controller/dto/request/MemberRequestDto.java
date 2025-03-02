package com.app.bdink.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberRequestDto(
        @NotBlank
        Long id,
        @NotBlank
        String name,
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}
