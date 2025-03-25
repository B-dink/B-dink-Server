package com.app.bdink.classroom.adapter.out.persistence.entity;

import com.app.bdink.classroom.adapter.in.controller.dto.request.ClassRoomDto;
import com.app.bdink.classroom.domain.Level;
import com.app.bdink.classroom.domain.PriceDetail;
import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.lecture.entity.Chapter;
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

    private String introLink;

    @Enumerated(EnumType.STRING)
    private Level level;

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.REMOVE)
    private List<Chapter> chapters = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Instructor instructor;

    @Embedded
    private PriceDetail priceDetail;


    @Builder
    public ClassRoomEntity(final String title, final String introduction,
                           final String thumbnail, final String introLink,
                           final Instructor instructor, final PriceDetail priceDetail, final Level level) {

        this.title = title;
        this.thumbnail = thumbnail;
        this.instructor = instructor;
        this.introduction = introduction;
        this.priceDetail = priceDetail;
        this.introLink = introLink;
        this.level = level;
    }

    //TODO: 도메인 로직꺼 호출하고 아래 싹 지우기.

    public void modifyClassRoom(final ClassRoomDto classRoomDto, final String thumbnailKey, final String videoKey){
        this.title = updateTitle(classRoomDto.title());
        this.introduction = updateIntroduction(classRoomDto.introduction());
        this.priceDetail = updatePriceDetail(classRoomDto.priceDto().toPriceDetail());
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

    public PriceDetail updatePriceDetail(final PriceDetail priceDetail) {
        if (priceDetail == null){
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

    public boolean isOwner(final Instructor instructor){
        return this.instructor.equals(instructor);
    }

    public void addChapter(final Chapter chapter){
        if (chapter == null){
            throw new IllegalStateException("chapter가 존재하지않습니다.");
        }
        this.chapters.add(chapter);
    }

    public void updateCDNLink(String cdnLink){
        this.introLink = cdnLink;
    }

    public boolean isEmptyThumbnail(){
        return this.thumbnail == null || this.thumbnail.isBlank();
    }
}
