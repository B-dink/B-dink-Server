package com.app.bdink.sugang.controller.dto.response;

import com.app.bdink.external.aws.lambda.domain.Media;
import com.app.bdink.sugang.entity.Sugang;

public record SugangInfoDto(
        Long sugangId,
        String m3u8_360Link,
        String m3u8_720Link,
        String mp4Link,
        String thumbnail

) {
    public static SugangInfoDto of(Media media, Sugang sugang){
        return new SugangInfoDto(
                sugang.getId(),
                media.getM3u8360Link(),
                media.getM3u8720Link(),
                media.getMp4Link(),
                media.getClassRoomThumbnail()
        );

    }
}
