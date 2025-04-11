package com.app.bdink.oauth2.apple.response;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;

import java.util.List;

public record ApplePublicKeys(List<ApplePublicKey> keys) {

    public ApplePublicKey getMatchesKey(String alg, String kid) {
        return this.keys
                .stream()
                .filter(k -> k.alg().equals(alg) && k.kid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new CustomException(Error.INVALID_APPLE_PUBLIC_KEY, Error.INVALID_APPLE_PUBLIC_KEY.getMessage()));
    }
}
