package com.app.bdink.member.entity;

import com.app.bdink.classroom.entity.Instructor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;
    
    @Column(name = "password")
    private String password;

    //TODO: 나중에 "M", "F" 이런식으로 바꿔주기
    @Column(name =  "gender")
    private boolean gender;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Instructor instructor;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "pictureUrl")
    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Builder
    public Member(String email, String password, String auth) {
        this.email = email;
        this.password = password;
    }
}
