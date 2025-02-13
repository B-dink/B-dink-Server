package com.app.bdink.classroom.entity;

import com.app.bdink.classroom.controller.dto.request.ClassRoomDto;
import com.app.bdink.classroom.domain.ChapterSummary;
import com.app.bdink.classroom.domain.PriceDetail;
import com.app.bdink.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;

    private String introduction;

    @Embedded
    private PriceDetail priceDetail;

    @Embedded
    private ChapterSummary chapterSummary;


    @Builder
    public ClassRoom(final String title, final String introduction,
                     final PriceDetail priceDetail, final ChapterSummary chapterSummary) {

        this.title = title;
        this.introduction = introduction;
        this.priceDetail = priceDetail;
        this.chapterSummary = chapterSummary;
    }

    public void modifyClassRoom(final ClassRoomDto classRoomDto){
        this.title = updateTitle(classRoomDto.title());
        this.introduction = updateIntroduction(classRoomDto.introduction());
        this.priceDetail = updatePriceDetail(classRoomDto.priceDto().toPriceDetail());
        //TODO: chapter 로직 추가
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

    public PriceDetail updatePriceDetail(final PriceDetail priceDetail) {
        if (priceDetail == null){
            return this.priceDetail;
        }
        this.priceDetail = priceDetail;
        return priceDetail;
    }
}
