package com.app.bdink.global.oauth2.apple.response;

public record TokenResponse(
        String access_token,
        int expires_in,
        String id_token,
        String refresh_token,
        String token_type
) {
}
