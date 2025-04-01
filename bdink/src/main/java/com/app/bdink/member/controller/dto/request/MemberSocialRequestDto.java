package com.app.bdink.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberSocialRequestDto(
        @NotBlank
        String phone,
        @NotBlank
        String name
) {
}
