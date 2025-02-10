package com.app.bdink.lecture.entity;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private ClassRoom classRoom;

    private String title;

    private LocalTime time;

    @Builder
    public Lecture(Member member, ClassRoom classRoom, String title, LocalTime time) {
        this.member = member;
        this.classRoom = classRoom;
        this.title = title;
        this.time = time;
    }
}
