package com.app.bdink.external.aws.lambda.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String s3Key;

    private Long classRoomId;

    private String m3u8360Link;

    private String m3u8720Link;

    @Enumerated(EnumType.STRING)
    private VideoType videoType;

    private Long lectureId;


    @Builder
    public Media(String s3Key, Long classRoomId, Long lectureId, String media360Key, String media720Key, VideoType videoType) {
        this.s3Key = s3Key;
        this.classRoomId = classRoomId;
        this.lectureId = lectureId;
        this.m3u8360Link = media360Key;
        this.m3u8720Link = media720Key;
        this.videoType = videoType;
    }
}
