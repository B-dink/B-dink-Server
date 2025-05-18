package com.app.bdink.member.controller.dto.request;

public record MemberLoginRequestDto(
        String email,
        String password
)
{
}
