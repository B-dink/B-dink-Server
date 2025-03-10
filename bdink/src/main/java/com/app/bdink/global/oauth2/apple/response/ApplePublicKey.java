package com.app.bdink.global.oauth2.apple.response;

public record ApplePublicKey(
        String kty,
        String kid,
        String use,
        String alg,
        String n,
        String e
) {
}
