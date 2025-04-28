package com.app.bdink.external.kollus.dto;

public record KollusTokenDTO(
        String kollusAccessToken
) {
    public static KollusTokenDTO of(final String kollusAccessToken) {
        return new KollusTokenDTO(kollusAccessToken);
    }
}
