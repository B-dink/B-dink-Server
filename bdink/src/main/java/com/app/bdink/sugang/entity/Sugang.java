package com.app.bdink.sugang.entity;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.member.entity.Member;
import com.app.bdink.sugang.controller.dto.SugangStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Sugang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClassRoomEntity classRoomEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    private SugangStatus sugangStatus;

    @Builder
    public Sugang(ClassRoomEntity classRoomEntity, Member member, SugangStatus sugangStatus) {
        this.classRoomEntity = classRoomEntity;
        this.member = member;
        this.sugangStatus = sugangStatus;
    }

//    public double updateProgress(){
//        if (this.progress > 100){
//            this.progress = 100;
//            completed = true;
//            return 100;
//        }
//        double totalSize = media.getTotalLength();
//        double presentSize = (double) totalSize / 10;
//        this.progress += presentSize *100;
//
//        if (this.progress > 100){
//            this.progress=100;
//            completed = true;
//        }
//
//        return this.progress;
//    }

}
