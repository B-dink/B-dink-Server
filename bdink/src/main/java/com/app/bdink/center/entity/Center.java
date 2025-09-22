package com.app.bdink.center.entity;

import com.app.bdink.center.controller.dto.CenterStatus;
import com.app.bdink.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Center extends BaseTimeEntity {
    /**
     * Center는 instructor의 상위 개념.
     * Center와 instructor는 1 : N의 관계
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "qrToken")
    private String qrToken;

//    @Column(name = "qrTokenExpiredAt")
//    private Long qrTokenExpiredAt;

    @Column(name = "profileImage")
    private String profileImage;

    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장 (예: "CONTRACTING", "TERMINATED")
    private CenterStatus status;

    @Builder
    public Center(String name, String address, String qrToken, Long qrTokenExpiredAt, String profileImage) {
        this.name = name;
        this.address = address;
        this.qrToken = qrToken;
//        this.qrTokenExpiredAt = qrTokenExpiredAt;
        this.profileImage = profileImage;
        this.status = CenterStatus.IN_PROGRESS;
    }

    // 엔티티 업데이트를 위한 메서드 추가
    public void updateName(String name) {
        this.name = name;
    }

    public void updateAddress(String address) {
        this.address = address;
    }

    public void updateQrToken(String qrToken) {
        this.qrToken = qrToken;
    }

//    public void updateQrTokenExpiredAt(Long qrTokenExpiredAt) {
//        this.qrTokenExpiredAt = qrTokenExpiredAt;
//    }

    public void updateStatus(CenterStatus status) {
        this.status = status;
    }
}
