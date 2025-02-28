package com.app.bdink.classroom.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private ClassRoom classRoom;

    @Builder
    public Bookmark(Member member, ClassRoom classRoom) {
        this.member = member;
        this.classRoom = classRoom;
    }
}
