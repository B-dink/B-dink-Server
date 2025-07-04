package com.app.bdink.external.kollus.entity;

import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class KollusMediaLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 사용자가 media를 보고있는지
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    // 어떤 KollusMedia(혹은 Media)를 시청했는지
    @ManyToOne(fetch = FetchType.LAZY)
    private KollusMedia kollusMedia; // or KollusMedia (선택)

    // 🔐 사용자 별 AccessToken (영상 재생용)
    @Column(length = 2048)
    private String accessToken;

    private LocalDateTime tokenCreatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    private double watchProgress;

    private boolean completed;

    private int playtimePercent;

    // 토큰 갱신용 메서드
    public void updateMediaToken(String accessToken, LocalDateTime tokenCreatedAt) {
        this.accessToken = accessToken;
        this.tokenCreatedAt = tokenCreatedAt;
    }

    public void updateWatchProgress(double watchProgress) {
        this.watchProgress = watchProgress;
    }

    public void updatePlaytimePercent(int playtimePercent) {
        this.playtimePercent = playtimePercent;
    }

    public void updateCompleted(boolean completed) {
        this.completed = completed;
    }
}



