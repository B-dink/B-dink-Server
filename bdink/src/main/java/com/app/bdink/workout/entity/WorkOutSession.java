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
public class WorkOutSession extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    // Session Name
    @Column(name = "memo")
    private String memo;

    //Session Memo
    @Column(name = "workoutMemo")
    private String workoutMemo;

    //Trainer Feedback
    @Column(name = "feedback")
    private String feedback;

    @OneToMany(mappedBy = "workOutSession",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PerformedExercise> performedExercises = new ArrayList<>();

    @Builder
    public WorkOutSession(Member member, String memo, String workoutMemo) {
        this.member = member;
        this.memo = memo;
        this.workoutMemo = workoutMemo;
        this.feedback = null;
    }

    public void changeMemo(String memo){
        this.memo = memo;
    }

    public void changeWorkoutMemo(String workoutMemo){
        this.workoutMemo = workoutMemo;
    }

    public void updateFeedback(String feedback){
        this.feedback = feedback;
    }

    public void addPerformedExercise(PerformedExercise pe){
        this.performedExercises.add(pe);
    }

    public void clearPerformedExercises(){
        this.performedExercises.clear();
    }
}
