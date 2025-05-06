package com.app.bdink.youtube.domain;

public record YoutubeInfoDto(
        String youtubeVideoUrl,
        YoutubeType youtubeType
) {
    public static YoutubeInfoDto of(final String youtubeVideoUrl, final YoutubeType youtubeType) {
        return new YoutubeInfoDto(youtubeVideoUrl, youtubeType);
    }
}
