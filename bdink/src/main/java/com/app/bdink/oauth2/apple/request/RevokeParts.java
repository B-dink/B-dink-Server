package com.app.bdink.oauth2.apple.request;

public record RevokeParts(
        String client_id,
        String client_secret,
        String code,
        String token_type_hint
) {
    public static RevokeParts of(String client_id, String client_secret, String code, String token_type_hint){
        return new RevokeParts(client_id, client_secret, code, token_type_hint);
    }
}
