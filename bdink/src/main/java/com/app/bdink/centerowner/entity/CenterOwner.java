package com.app.bdink.centerowner.entity;

import com.app.bdink.center.entity.Center;
import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 센터장 소속 정보를 관리하는 엔티티.
 * Center N:1, Member N:1 관계이며 soft delete는 status로 관리한다.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "CenterOwner", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"center_id", "member_id"})
})
public class CenterOwner extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    private Center center;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    // soft delete용 상태값
    private CenterOwnerStatus status;

    @Builder
    public CenterOwner(Center center, Member member) {
        this.center = center;
        this.member = member;
        this.status = CenterOwnerStatus.ACTIVE;
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
        this.status = CenterOwnerStatus.INACTIVE;
    }

    /**
     * 비활성 센터장을 재활성화한다.
     */
    public void activate() {
        this.status = CenterOwnerStatus.ACTIVE;
    }
}
