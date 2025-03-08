package com.app.bdink.member.controller.dto.response;

import com.app.bdink.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberLoginResponseDto(
        String email,
        String token
) {
    public static MemberLoginResponseDto of(Member member, String token)
    {
        return MemberLoginResponseDto.builder()
                .email(member.getEmail())
                .token(token)
                .build();
    }
}
