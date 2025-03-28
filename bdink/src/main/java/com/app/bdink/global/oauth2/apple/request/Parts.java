package com.app.bdink.global.oauth2.apple.request;

public record Parts(
        String client_id,
        String client_secret,
        String code,
        String grant_type
) {
    public static Parts of(String client_id, String client_secret, String code, String grant_type){
        return new Parts(client_id, client_secret, code, grant_type);
    }
}
