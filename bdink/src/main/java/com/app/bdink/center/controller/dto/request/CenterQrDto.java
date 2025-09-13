package com.app.bdink.center.controller.dto.request;

public record CenterQrDto(
        Long centerId,
        Long classroomId,
        String qrToken
) {
    public static CenterQrDto of(Long centerId, Long classroomId, String qrToken){
        return new CenterQrDto(centerId, classroomId, qrToken);
    }
}
