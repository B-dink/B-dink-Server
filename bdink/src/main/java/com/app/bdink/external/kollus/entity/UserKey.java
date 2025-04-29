package com.app.bdink.external.kollus.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Table(name = "Userkey")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserKey extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_key", nullable = false, length = 255, unique = true)
    private String userKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "is_revoked", nullable = false)
    private boolean isRevoked;

    @Builder
    public UserKey(String userKey) {
        this.userKey = userKey;
        this.member = null;
        this.assignedAt = null;
        this.isRevoked = false;
    }

    public void updateMember(Member member) {
        this.member = member;
    }

    public void updateAssignedAt() {
        this.assignedAt = LocalDateTime.now();
    }

    public void updateIsRevoked() {
        this.isRevoked = true;
    }
    
    //mapping 로직
    public static UserKey of(String userKey) {
        return UserKey.builder()
                .userKey(userKey)
                .build();
    }
}
