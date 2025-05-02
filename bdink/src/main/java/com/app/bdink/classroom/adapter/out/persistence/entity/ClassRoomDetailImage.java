package com.app.bdink.classroom.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ClassRoomDetailImage")
public class ClassRoomDetailImage {

    @Id
    @GeneratedValue
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClassRoomEntity classRoom;

    private int sortOrder; // 이미지 순서
}
