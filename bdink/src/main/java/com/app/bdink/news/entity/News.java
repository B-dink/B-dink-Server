package com.app.bdink.news.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.news.controller.dto.request.NewsReqDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class News extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "url")
    private String url;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Builder
    public News(String title, String content, String url, String thumbnailUrl) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void update(NewsReqDto newsReqDto) {
        this.title = updateTitle(newsReqDto.title());
        this.content = updateContent(newsReqDto.content());
        this.url = updateUrl(newsReqDto.url());
        this.thumbnailUrl = updateThumbnailUrl(newsReqDto.thumbnailUrl());
    }

    public String updateTitle(String title) {
        if (title.isBlank() || title == null)
            return this.title;
        return title;
    }

    public String updateContent(String content) {
        if (content.isBlank() || content == null)
            return this.content;
        return content;
    }

    public String updateUrl(String url) {
        if (url.isBlank() || url == null)
            return this.url;
        return url;
    }

    public String updateThumbnailUrl(String thumbnailUrl) {
        if (thumbnailUrl.isBlank() || thumbnailUrl == null)
            return this.thumbnailUrl;
        return thumbnailUrl;
    }

}
