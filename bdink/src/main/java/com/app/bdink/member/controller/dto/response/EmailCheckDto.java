package com.app.bdink.member.controller.dto.response;

public record EmailCheckDto(
        boolean isCorrect
) {
    public static EmailCheckDto from(boolean isCorrect){
        return new EmailCheckDto(isCorrect);
    }
}
