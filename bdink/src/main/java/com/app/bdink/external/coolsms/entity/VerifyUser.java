package com.app.bdink.external.coolsms.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VerifyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String phone;

    @Column(name = "sms_code")
    private String smsCode;

    public VerifyUser(String phone, String smsCode) {
        this.phone = phone;
        this.smsCode = smsCode;
    }

}
