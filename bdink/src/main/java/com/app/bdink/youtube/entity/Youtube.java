package com.app.bdink.youtube.entity;

import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.youtube.domain.YoutubeType;
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

    @Enumerated(EnumType.STRING)
    private YoutubeType youtubeType;

    @OneToOne(fetch = FetchType.LAZY)
    private Instructor instructor;

    @Builder
    public Youtube(String youtubeVideoLink, YoutubeType youtubeType, Instructor instructor) {
        this.youtubeVideoLink = youtubeVideoLink;
        this.youtubeType = youtubeType;
        this.instructor = instructor;
    }

    public void updateYoutubeVideoLink(String youtubeVideoLink) {
        this.youtubeVideoLink = youtubeVideoLink;
    }
}
