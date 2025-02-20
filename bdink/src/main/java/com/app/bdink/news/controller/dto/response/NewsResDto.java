package com.app.bdink.news.controller.dto.response;

import com.app.bdink.news.entity.News;

public record NewsResDto(
    String title,
    String content,
    String url,
    String thumbnailUrl
) {
    public NewsResDto(News news) {
        this(news.getTitle(), news.getContent(), news.getUrl(), news.getThumbnailUrl());
    }
}
