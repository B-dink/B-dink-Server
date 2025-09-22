package com.app.bdink.classroom.adapter.in.controller.dto.request;

public record ClassRoomQrDto(
        String classRoomQrToken
) {
    public static ClassRoomQrDto of(String qrToken){
        return new ClassRoomQrDto(qrToken);

    }
}

