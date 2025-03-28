package com.app.bdink.payment.domain;

public record Failure(
        String code,
        String message
) {
}
