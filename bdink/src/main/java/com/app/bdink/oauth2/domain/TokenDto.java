package com.app.bdink.oauth2.domain;

public record TokenDto(
        String accessToken,
        String refreshToken
) {
    public static TokenDto of(final String accessToken, final String refreshToken) {
        return new TokenDto(accessToken, refreshToken);
    }

}
