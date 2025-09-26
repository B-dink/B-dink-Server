package com.app.bdink.classroom.adapter.out.persistence.entity;

import com.app.bdink.chapter.entity.Chapter;
import com.app.bdink.classroom.adapter.in.controller.dto.request.ClassRoomDto;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.classroom.domain.Level;
import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.price.domain.PriceDetail;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ClassRoom")
public class ClassRoomEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;

    private String introduction;

    private String thumbnail;

    /**
     * 일단 확인된 필요한건 4가지
     * 프로모션 썸네일
     * 강의 썸네일 -> 이걸 기존 thumbnail이라고 치자
     * 디테일 썸네일
     */

    private String promotionThumbnail;

    private String detailThumbnail;

    private String introLink;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Enumerated(EnumType.STRING)
    private Career career;

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.REMOVE)
    private List<Chapter> chapters = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Instructor instructor;

    @Embedded
    private PriceDetail priceDetail;

    private String subtitles;

    @Column(name = "qrToken", nullable = true)
    private String qrToken;

    //PROMOTION FLAG
    private int promotionOf;

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassRoomDetailImage> detailPageImages = new ArrayList<>();

    @Builder
    public ClassRoomEntity(final String title, final String introduction,
                           final String thumbnail, final String introLink,
                           final String promotionThumbnail, final String detailThumbnail,
                           final String subtitles,
                           final Instructor instructor, final PriceDetail priceDetail, final Level level, final Career career) {

        this.title = title;
        this.thumbnail = thumbnail;
        this.promotionThumbnail = promotionThumbnail;
        this.detailThumbnail = detailThumbnail;
        this.instructor = instructor;
        this.introduction = introduction;
        this.priceDetail = priceDetail;
        this.introLink = introLink;
        this.subtitles = subtitles;
        this.level = level;
        this.career = career;
    }

    //TODO: 도메인 로직꺼 호출하고 아래 싹 지우기.

    public void modifyClassRoom(final ClassRoomDto classRoomDto, final String thumbnailKey, final String videoKey) {
        this.title = updateTitle(classRoomDto.title());
        this.introduction = updateIntroduction(classRoomDto.introduction());
        this.priceDetail = updatePriceDetail(classRoomDto.priceDto().toPriceDetail());
        this.thumbnail = thumbnailKey; //TODO: 이미지 관련해서 어떤 예외가 생길 수 있는지?
        this.introLink = videoKey;
        this.level = updateLevel(classRoomDto.level());
        this.career = updateCarrer(classRoomDto.career());
    }

    public String updateTitle(final String title) {
        if (title.isBlank() || title == null) {
            return this.title; //필드가 비어있으면 수정하지 않음.
        }
        this.title = title;
        return this.title;
    }

    public String updateIntroduction(final String introduction) {
        if (introduction.isBlank() || introduction == null) {
            return this.introduction;
        }
        this.introduction = introduction;
        return this.introduction;
    }

    public PriceDetail updatePriceDetail(final PriceDetail priceDetail) {
        if (priceDetail == null) {
            return this.priceDetail;
        }
        this.priceDetail = priceDetail;
        return priceDetail;
    }

    public Level updateLevel(final Level level) {
        if (level == null) {
            return this.level;
        }
        this.level = level;
        return this.level;
    }

    public Career updateCarrer(final Career career) {
        if (career == null) {
            return this.career;
        }
        this.career = career;
        return this.career;
    }

    public boolean isOwner(final Instructor instructor) {
        return this.instructor.equals(instructor);
    }

    public void addChapter(final Chapter chapter) {
        if (chapter == null) {
            throw new CustomException(Error.NOT_FOUND_CHAPTER, Error.NOT_FOUND_CHAPTER.getMessage());
        }
        this.chapters.add(chapter);
    }

    public void updateCDNLink(String cdnLink) {
        this.introLink = cdnLink;
    }

    public boolean isEmptyThumbnail() {
        return this.thumbnail == null || this.thumbnail.isBlank();
    }

    public Void softDeleteInstructor() {
        this.instructor = null;
        return null;
    }
}
