package com.app.bdink.news.controller.dto.request;

import com.app.bdink.news.entity.News;

public record NewsReqDto(
    String title,
    String content,
    String url,
    String thumbnailUrl
) {
public News toEntity() {
    return News.builder()
        .title(title)
        .content(content)
        .url(url)
        .thumbnailUrl(thumbnailUrl)
        .build();
    }
}
