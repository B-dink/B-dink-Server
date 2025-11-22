package com.app.bdink.workout.entity;

import com.app.bdink.workout.controller.dto.ExercisePart;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    private ExercisePart part;

    @Builder
    public Exercise(String name, String description, String videoUrl, String pictureUrl, ExercisePart part) {
        this.name = name;
        this.description = description;
        this.videoUrl = videoUrl;
        this.pictureUrl = pictureUrl;
        this.part = part;
    }
}
