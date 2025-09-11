package com.app.bdink.center.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @Column(name = "qrTokenExpiredAt")
    private Long qrTokenExpiredAt;
}
