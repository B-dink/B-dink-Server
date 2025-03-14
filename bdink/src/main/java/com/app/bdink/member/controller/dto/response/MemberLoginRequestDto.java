package com.app.bdink.member.controller.dto.response;

public record MemberLoginRequestDto(
        String email,
        String password
)
{
}
