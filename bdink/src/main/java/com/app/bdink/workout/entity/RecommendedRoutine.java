package com.app.bdink.workout.entity;

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
public class RecommendedRoutine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @OneToMany(mappedBy = "routine",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<RoutineExercise> exercises = new ArrayList<>();

    @Builder
    public RecommendedRoutine(String title, String description, String thumbnailUrl) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void clearExercises() {
        this.exercises.clear(); // orphanRemoval 때문에 자식들 삭제
    }

    public void addExercise(RoutineExercise exercise) {
        this.exercises.add(exercise);
        exercise.setRoutine(this);
    }
}
