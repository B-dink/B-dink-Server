package com.app.bdink.trainer.entity;

import com.app.bdink.center.entity.Center;
import com.app.bdink.classroom.domain.Career;
import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 센터 소속 트레이너 프로필 엔티티.
 * Member 1:1, Center N:1 관계를 가지며 QR 토큰과 soft delete는 status로 관리한다.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Trainer", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id"})
})
public class Trainer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    private Center center;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "career", nullable = false)
    private Career career;

    @Column(name = "intro")
    private String intro;

    @Column(name = "profileImage")
    private String profileImage;

    @Column(name = "qrToken")
    private String qrToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    // soft delete용 상태값
    private TrainerStatus status;

    @Builder
    public Trainer(Center center, Member member, String name, Career career, String intro, String profileImage) {
        this.center = center;
        this.member = member;
        this.name = name;
        this.career = career;
        this.intro = intro;
        this.profileImage = profileImage;
        this.status = TrainerStatus.ACTIVE;
    }

    /**
     * 전달된 값만 선택적으로 갱신한다. (null은 무시)
     */
    public void update(String name, Career career, String intro, String profileImage) {
        if (name != null) {
            this.name = name;
        }
        if (career != null) {
            this.career = career;
        }
        if (intro != null) {
            this.intro = intro;
        }
        if (profileImage != null) {
            this.profileImage = profileImage;
        }
    }

    /**
     * 트레이너 QR 토큰을 갱신한다. null 허용.
     */
    public void updateQrToken(String qrToken) {
        this.qrToken = qrToken;
    }

    /**
     * 소속 센터를 변경한다.
     */
    public void updateCenter(Center center) {
        this.center = center;
    }

    /**
     * soft delete 처리.
     */
    public void deactivate() {
        this.status = TrainerStatus.INACTIVE;
    }

    /**
     * 비활성 트레이너를 재활성화한다.
     */
    public void activate() {
        this.status = TrainerStatus.ACTIVE;
    }
}
