package com.app.bdink.oauth2.domain;

public record DoubleCheckResponse(
        boolean isChecked
) {
    public static DoubleCheckResponse from(boolean isChecked){
        return new DoubleCheckResponse(isChecked);
    }
}
