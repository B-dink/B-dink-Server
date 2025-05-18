package com.app.bdink.member.controller.dto.response;

public record UpdateProfileDto(
        String result
) {
    public static UpdateProfileDto from(String result) {
        return new UpdateProfileDto(result);
    }
}

