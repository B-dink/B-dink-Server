package com.app.bdink.lecture.entity;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Chapter extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classRoom_id")
    private ClassRoom classRoom;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.REMOVE)
    private List<Lecture> lectures = new ArrayList<>();

    private String title; // 챕터 제목

    private int lectureCount; // 해당 챕터에 속한 강좌의 수

    private int number;

    public Chapter(ClassRoom classRoom, String title) {
        this.classRoom = classRoom;
        this.title = title;
        this.lectureCount = 0;
        this.number = 1;
    }

    public void increaseLectureCount() {
        this.lectureCount++;
    }

    public void decreaseLectureCount(){
        this.lectureCount--;
    }

    public void updateNumber(){
        this.number++;
    }

    public void addLectures(final Lecture lecture){
        this.lectures.add(lecture);
    }


}
