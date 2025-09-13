package com.app.bdink.instructor.adapter.out.persistence.entity;

import com.app.bdink.center.entity.Center;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
//강사만이 클래스룸 및 강좌를 등록할 수 있다.
@Table(name = "Instructor")
public class Instructor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // ManyToOne 관계 설정
    @JoinColumn(name = "center_id") // 외래키 컬럼
    private Center center; // Center 엔티티 참조 필드 추가

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Career career;

    private String marketingImage;

    private String marketingText;

    private String marketingSites;

    @Builder
    public Instructor(Member member, Career career,
                      String marketingImage, String marketingText, String marketingSites) {
        this.member = member;
        this.career = career;
        this.marketingImage = marketingImage;
        this.marketingSites = marketingSites;
        this.marketingText = marketingText;
    }

    public void modify(final Career career, String marketingImage, String marketingSites, String marketingText){
        this.career = career;
        this.marketingText = marketingText;
        this.marketingSites = marketingSites;
        this.marketingImage = marketingImage;
    }

    public boolean isEmptyThumbnail(){
        return this.marketingImage == null || this.marketingImage.isBlank();
    }

    public void softDelete(){
        this.career = null;
    }
}
