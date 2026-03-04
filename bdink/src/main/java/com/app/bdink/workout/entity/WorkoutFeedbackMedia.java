package com.app.bdink.workout.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WorkoutFeedbackMedia extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id", nullable = false)
    private WorkoutFeedback feedback;

    @Enumerated(EnumType.STRING)
    @Column(name = "mediaType", nullable = false)
    private WorkoutFeedbackMediaType mediaType;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "mediaOrder")
    private Integer mediaOrder;

    @Column(name = "thumbnailUrl")
    private String thumbnailUrl;

    @Builder
    public WorkoutFeedbackMedia(
            WorkoutFeedback feedback,
            WorkoutFeedbackMediaType mediaType,
            String url,
            Integer mediaOrder,
            String thumbnailUrl
    ) {
        this.feedback = feedback;
        this.mediaType = mediaType;
        this.url = url;
        this.mediaOrder = mediaOrder;
        this.thumbnailUrl = thumbnailUrl;
    }
}
