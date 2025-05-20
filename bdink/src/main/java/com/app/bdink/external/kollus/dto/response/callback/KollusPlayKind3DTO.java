package com.app.bdink.external.kollus.dto.response.callback;

public record KollusPlayKind3DTO(
        Data data,
        long exp
) {
    public record Data(
            Integer content_expired,
            int result
    ) {}

    public static KollusPlayKind3DTO ofKind3(int contentExpired, int result, long exp) {
        return new KollusPlayKind3DTO(
                new Data(contentExpired, result),
                exp
        );
    }
}
