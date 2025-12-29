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
public class ExerciseAlias extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // 원본 운동과의 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    private Exercise exercise;

    // 수동으로 등록한 별칭
    @Column(name = "alias", nullable = false)
    private String alias;

    @Builder
    public ExerciseAlias(String alias) {
        this.alias = alias;
    }

    // 연관관계 편의 메서드
    void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}
