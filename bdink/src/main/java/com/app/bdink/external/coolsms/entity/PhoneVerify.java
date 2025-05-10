package com.app.bdink.external.coolsms.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PhoneVerify extends BaseTimeEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = UUID.randomUUID();

    private String phone;

    private String verifyCode;


    @Builder
    public PhoneVerify(String phone, String verifyCode) {
        this.phone = phone;
        this.verifyCode = verifyCode;
    }

    public boolean isExpired() {
        return getCreatedAt().plusMinutes(3).isBefore(LocalDateTime.now());
    }
}
