package com.app.bdink.nutrient.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
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
public class Nutrient extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "like_count")
    private int likeCount;

    @Column(name = "comment_count")
    private int commentCount;

    @Builder
    public Nutrient(String title, String content, String imgUrl, int likeCount, int commentCount) {
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public void update(String title, String content, String imgUrl) {
        this.title = updateTitle(title);
        this.content = updateContent(content);
        this.imgUrl = updateImgUrl(imgUrl);
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

    public String updateImgUrl(String imgUrl) {
        if (imgUrl.isBlank() || imgUrl == null)
            return this.imgUrl;
        return imgUrl;
    }

}
