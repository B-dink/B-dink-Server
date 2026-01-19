package com.app.bdink.trainermember.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.member.entity.Member;
import com.app.bdink.trainer.entity.Trainer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 트레이너-멤버 소속 관계를 관리하는 엔티티.
 * Member는 1명의 트레이너에만 소속될 수 있도록 member_id 유니크를 사용한다.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "TrainerMember", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id"})
})
public class TrainerMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    // soft delete용 상태값
    private TrainerMemberStatus status;

    @Builder
    public TrainerMember(Trainer trainer, Member member) {
        this.trainer = trainer;
        this.member = member;
        this.status = TrainerMemberStatus.ACTIVE;
    }

    /**
     * 소속 트레이너를 변경한다.
     */
    public void updateTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    /**
     * soft delete 처리.
     */
    public void deactivate() {
        this.status = TrainerMemberStatus.INACTIVE;
    }

    /**
     * 비활성 소속을 재활성화한다.
     */
    public void activate() {
        this.status = TrainerMemberStatus.ACTIVE;
    }
}
