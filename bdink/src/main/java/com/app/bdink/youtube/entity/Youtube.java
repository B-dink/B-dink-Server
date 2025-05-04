package com.app.bdink.youtube.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Youtube")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Youtube {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String youtubeVideoLink;

    @Builder
    public Youtube(String youtubeVideoLink) {
        this.youtubeVideoLink = youtubeVideoLink;
    }

    public void updateYoutubeVideoLink(String youtubeVideoLink) {
        this.youtubeVideoLink = youtubeVideoLink;
    }
}
