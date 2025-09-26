package com.app.bdink.classroom.domain;

import com.app.bdink.classroom.adapter.in.controller.dto.request.ClassRoomDto;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClassRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String introduction;

    private String thumbnail;

    private String promotionThumbnail;

    private String detailThumbnail;

    private String otLink;

    private String introLink;

    @Enumerated(EnumType.STRING)
    private Level level;

    public void modifyClassRoom(final ClassRoomDto classRoomDto, final String thumbnailKey, final String videoKey){
        this.title = updateTitle(classRoomDto.title());
        this.introduction = updateIntroduction(classRoomDto.introduction());
        this.thumbnail = thumbnailKey; //TODO: 이미지 관련해서 어떤 예외가 생길 수 있는지?
        this.introLink = videoKey;
        this.level = updateLevel(classRoomDto.level());
    }

    public String updateTitle(final String title) {
        if (title.isBlank() || title == null){
            return this.title; //필드가 비어있으면 수정하지 않음.
        }
        this.title = title;
        return this.title;
    }

    public String updateIntroduction(final String introduction) {
        if (introduction.isBlank() || introduction == null){
            return this.introduction;
        }
        this.introduction = introduction;
        return this.introduction;
    }

    public Level updateLevel(final Level level) {
        if (level == null) {
            return this.level;
        }
        this.level = level;
        return this.level;
    }

    public void updateCDNLink(String cdnLink){
        this.introLink = cdnLink;
    }

    public boolean isEmptyThumbnail(){
        return this.thumbnail == null || this.thumbnail.isBlank();
    }
}
