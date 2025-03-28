package com.app.bdink.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberSocialRequestDto(
        @NotBlank
        String email,
        @NotBlank
        String password,
        boolean passwordDoubleCheck,
        @NotBlank
        String name
) {
}
