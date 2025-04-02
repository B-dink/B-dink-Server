package com.app.bdink.chapter.entity;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.lecture.entity.Lecture;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private ClassRoomEntity classRoom;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.REMOVE)
    private List<Lecture> lectures = new ArrayList<>();

    private String title; // 챕터 제목

    // TODO: lectureCount 필드는 사용하지 않는 것으로 보입니다. 제거해도 될 것 같습니다.
    private int lectureCount; // 해당 챕터에 속한 강좌의 수

    private int number;

    public Chapter(ClassRoomEntity classRoomEntity, String title) {
        this.classRoom = classRoomEntity;
        this.title = title;
        this.lectureCount = 0;
        this.number = 1;
    }

    public void updateNumber(){
        this.number++;
    }

    public void addLectures(final Lecture lecture){
        this.lectures.add(lecture);
    }


}
