package com.app.bdink.payment.toss.controller.dto;

public record CancelRequest(
        String cancelReason,
        Integer cancelAmount,
        Long cancelClassRoomId
) {}