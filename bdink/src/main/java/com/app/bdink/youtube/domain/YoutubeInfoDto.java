package com.app.bdink.youtube.domain;

public record YoutubeInfoDto(
        String youtubeVideoUrl
) {
    public static YoutubeInfoDto of(final String youtubeVideoUrl) {
        return new YoutubeInfoDto(youtubeVideoUrl);
    }
}
