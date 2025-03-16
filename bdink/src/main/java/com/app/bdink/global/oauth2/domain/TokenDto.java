package com.app.bdink.global.oauth2.domain;

import com.app.bdink.member.entity.Member;

public record TokenDto(
        String accessToken,
        String refreshToken
) {
    public static TokenDto of(final String accessToken, final String refreshToken) {
        return new TokenDto(accessToken, refreshToken);
    }

}
