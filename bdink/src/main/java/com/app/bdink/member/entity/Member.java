package com.app.bdink.member.entity;

import com.app.bdink.classroom.domain.Career;
import com.app.bdink.classroom.adapter.out.persistence.entity.Instructor;
import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.oauth2.domain.EmailValidator;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;
    
    @Column(name = "password")
    private String password;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Instructor instructor;

    @Column(name = "email")
    private String email;

    private Long kakaoId;

    private String appleId;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "pictureUrl")
    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    private String refreshToken;

    @Builder
    public Member(String email, String password, String name, Role role, String phoneNumber, String pictureUrl,
                  String appleId, Long kakaoId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.pictureUrl = pictureUrl;
        this.appleId = appleId;
        this.kakaoId = kakaoId;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateRefreshToken(String refreshToken){this.refreshToken = refreshToken;}

    public void updatePassword(String password) {
        this.password = password;
    }

    public Career getInterest(){
        if (this.instructor != null){
            return instructor.getCareer();
        }
        return Career.EXERCISE;
    }

    public void modifyingInSocialSignUp(String name, String email){
        this.name = name;
        this.email = email;
        this.role = Role.ROLE_USER;
    }

    public void delete(){
        this.name = null;
        this.password = null;
        this.instructor = null;
        this.email = "";
        this.phoneNumber = "";
        this.appleId = null;
        this.kakaoId = null;
        this.pictureUrl = null;
        this.refreshToken =null;
    }
}
