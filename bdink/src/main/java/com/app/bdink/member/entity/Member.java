package com.app.bdink.member.entity;

import com.app.bdink.classroom.entity.Instructor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;
    //TODO: 나중에 "M", "F" 이런식으로 바꿔주기
    @Column(name =  "gender")
    private boolean gender;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Instructor instructor;

}
