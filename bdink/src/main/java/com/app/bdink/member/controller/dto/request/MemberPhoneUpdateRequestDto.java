package com.app.bdink.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MemberPhoneUpdateRequestDto(
        @NotBlank
        @Pattern(regexp = "^01[0-9]{8,9}$", message = "전화번호 형식이 올바르지 않습니다.")
        String phoneNumber
) {
}