package com.app.bdink.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MemberSocialRequestDto(
        @NotBlank
        String phone,
        @NotBlank
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,8}$", message = "이름은 특수문자를 제외한 2~8글자여야 합니다.")
        String name
) {
}
