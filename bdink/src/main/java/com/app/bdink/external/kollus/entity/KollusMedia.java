package com.app.bdink.external.kollus.entity;

import com.app.bdink.lecture.entity.Lecture;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class KollusMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 강의에 대한 미디어인지 (1:1)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = true, unique = true)
    private Lecture lecture;

    // Kollus에서 발급한 고유 키
    @Column(nullable = false, unique = true)
    private String mediaContentKey;

    @Column(name = "channel_key")
    private String channelKey;

    @Column(name = "channel_name")
    private String channelName;

    @Column(name = "upload_file_key")
    private String uploadFileKey;

    private String filename;

    // 영상 총 길이
    private double totalLength;

    // 총 길이만 변경 가능하도록 메서드
    public void updateTotalLength(double totalLength) {
        this.totalLength = totalLength;
    }

    public void updateLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}
