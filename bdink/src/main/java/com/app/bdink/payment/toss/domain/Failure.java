package com.app.bdink.payment.toss.domain;

public record Failure(
        String code,
        String message
) {
}
