package com.app.bdink.payment.controller.dto;

public record CancelRequest(
        String cancelReason,
        Integer cancelAmount
) {}