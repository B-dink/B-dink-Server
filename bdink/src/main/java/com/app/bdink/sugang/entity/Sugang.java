package com.app.bdink.sugang.entity;

import com.app.bdink.external.aws.lambda.domain.Media;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Sugang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Media media;

    @Getter
    @Column(name = "completed")
    private boolean completed;

    private double progress;

    @Builder
    public Sugang(Lecture lecture, Member member, Media media) {
        this.lecture = lecture;
        this.member = member;
        this.media = media;
        this.completed = false;  // 기본값: 수강하지 않은 상태
        this.progress = 0;
    }

    public double updateProgress(){
        int totalSize = media.getTotalLength();
        double presentSize = (double) totalSize / 10;
        this.progress += presentSize *100;
        return this.progress;
    }

}
