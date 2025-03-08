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

    private Long lectureId;


    @Builder
    public Media(String s3Key, Long classRoomId, Long lectureId) {
        this.s3Key = s3Key;
        this.classRoomId = classRoomId;
        this.lectureId = lectureId;
    }
}
