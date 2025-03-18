package com.app.bdink.common.util;

public record CreateIdDto(
        Long id
) {
    public static CreateIdDto from(String id){
        return new CreateIdDto(Long.parseLong(id));
    }
}
