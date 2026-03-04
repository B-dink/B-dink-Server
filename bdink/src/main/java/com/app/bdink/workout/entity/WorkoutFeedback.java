package com.app.bdink.workout.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkoutFeedback extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workOutSession_id", nullable = false)
    private WorkOutSession workOutSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private Member trainer;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutFeedbackMedia> mediaList = new ArrayList<>();

    @Builder
    public WorkoutFeedback(WorkOutSession workOutSession, Member trainer, String content) {
        this.workOutSession = workOutSession;
        this.trainer = trainer;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void addMedia(WorkoutFeedbackMedia media) {
        this.mediaList.add(media);
    }

    public void clearMedia() {
        this.mediaList.clear();
    }
}
