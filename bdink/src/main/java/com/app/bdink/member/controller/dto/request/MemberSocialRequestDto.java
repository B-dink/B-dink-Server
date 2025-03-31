package com.app.bdink.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record MemberSocialRequestDto(
        @NotBlank
        String phone,
        @NotBlank
        String name
) {
}
