package com.app.bdink.lecture.entity;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.sugang.entity.Sugang;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClassRoomEntity classRoom; //TODO: 얘 뭐지 흠.. 당장 문제는 안될거같긴한데

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @Column(name = "title")
    private String title;

    @Column(name = "time")
    private LocalTime time;

    @Column(name = "media_link")
    private String mediaLink;

    @OneToMany(mappedBy = "lecture")
    private List<Sugang> sugangs; // 수강 신청 목록

    public List<Sugang> getSugangs() {
        return sugangs;
    }

    @Builder
    public Lecture(ClassRoomEntity classRoom, Chapter chapter, String title, LocalTime time, String mediaLink) {
        this.classRoom = classRoom;
        this.chapter = chapter;
        this.title = title;
        this.time = time;
        this.mediaLink = mediaLink;
    }
}
