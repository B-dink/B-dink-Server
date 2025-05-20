package com.app.bdink.external.kollus.dto.response.callback;

public record KollusPlayKind1DTO(
        Data data,
        long exp
) {
    public record Data(
            Long expiration_date,
            int result
    ) {}
    public static KollusPlayKind1DTO ofKind1(long expirationDate, int result, long exp) {
        return new KollusPlayKind1DTO(
                new Data(expirationDate, result),
                exp
        );
    }
}
