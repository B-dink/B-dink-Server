package com.app.bdink.member.entity;

import com.app.bdink.classroom.domain.Career;
import com.app.bdink.instructor.adapter.out.persistence.entity.Instructor;
import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.oauth2.domain.SocialType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "pictureUrl")
    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    private String refreshToken;

    @Column(columnDefinition = "boolean default false")
    private boolean eventAgree;

    @Column(name = "user_key", length = 1000) //Todo: 콜러스에서 받아온 유저의 키 -> 추후에 다른 entity로 옮길수도..
    private String userKey;

    @Column(unique = true)
    private String kollusClientUserId;

    @Builder
    public Member(String email, String password, String name, Role role, String phoneNumber, String pictureUrl,
                  String appleId, Long kakaoId, SocialType socialType, boolean eventAgree) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.pictureUrl = pictureUrl;
        this.appleId = appleId;
        this.kakaoId = kakaoId;
        this.socialType = socialType;
        this.eventAgree = eventAgree;
        this.userKey = null;
        this.kollusClientUserId = socialType+"_"+UUID.randomUUID();
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateRefreshToken(String refreshToken){this.refreshToken = refreshToken;}

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateEventAgree(boolean eventAgree){
        this.eventAgree = eventAgree;
    }

    public void updateName(String name) { this.name = name; }

    public void updatePictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }

    public void updateUserKey(String userKey){ this.userKey = userKey; }

    public Career getInterest(){
        if (this.instructor != null){
            return instructor.getCareer();
        }
        return Career.EXERCISE;
    }

    public void modifyingInSocialSignUp(String name, String profileImage, String phone){
        this.name = name;
        this.pictureUrl = profileImage;
        this.phoneNumber = phone;
        this.role = Role.ROLE_USER;
    }

    public void delete(){
        this.role = Role.DELETE_USER;
        this.password = null;
        this.instructor = null;
        this.email = "";
        this.phoneNumber = "";
        this.appleId = null;
        this.kakaoId = null;
        this.pictureUrl = null;
        this.refreshToken =null;
        this.eventAgree = false;
    }
}
