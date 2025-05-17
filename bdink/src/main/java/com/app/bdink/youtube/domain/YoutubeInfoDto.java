package com.app.bdink.youtube.domain;

public record YoutubeInfoDto(
        String youtubeVideoUrl,
        YoutubeType youtubeType,
        Long instructorId
) {
    public static YoutubeInfoDto of(final String youtubeVideoUrl, final YoutubeType youtubeType, final Long instructorId) {
        return new YoutubeInfoDto(youtubeVideoUrl, youtubeType, instructorId);
    }
}
