package com.app.bdink.lecture.entity;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.classroom.entity.Instructor;
import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private ClassRoom classRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    private String title;

    private LocalTime time;

    @Builder
    public Lecture(ClassRoom classRoom, Chapter chapter, String title, LocalTime time) {
        this.classRoom = classRoom;
        this.chapter = chapter;
        this.title = title;
        this.time = time;
    }
}
