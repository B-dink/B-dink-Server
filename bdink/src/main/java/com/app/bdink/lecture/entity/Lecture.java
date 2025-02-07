package com.app.bdink.lecture.entity;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private ClassRoom classRoom;

}
