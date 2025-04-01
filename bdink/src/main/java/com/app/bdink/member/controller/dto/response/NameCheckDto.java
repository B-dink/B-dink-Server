package com.app.bdink.member.controller.dto.response;

public record NameCheckDto(
        boolean isCorrect
) {
    public static NameCheckDto from(boolean isCorrect){
        return new NameCheckDto(isCorrect);
    }
}
