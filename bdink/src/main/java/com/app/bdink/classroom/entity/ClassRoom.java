package com.app.bdink.classroom.entity;

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
}
