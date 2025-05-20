package com.app.bdink.external.kollus.dto.response.callback;

public record KollusPlayResponse(
        Data data,
        long exp
) {
    public record Data(
            Integer content_expired,
            Long expiration_date,
            int result
    ) {}

    public static KollusPlayResponse ofKind3(int contentExpired, int result, long exp) {
        return new KollusPlayResponse(
                new Data(contentExpired, null, result),
                exp
        );
    }

    public static KollusPlayResponse ofKind1(long expirationDate, int result, long exp) {
        return new KollusPlayResponse(
                new Data(null, expirationDate, result),
                exp
        );
    }
}
