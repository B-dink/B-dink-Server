package com.app.bdink.qna.entity;

import com.app.bdink.classroom.entity.ClassRoom;
import com.app.bdink.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import javax.xml.crypto.dsig.SignatureProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    private ClassRoom classRoom;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    @Builder
    public Question(String content, ClassRoom classRoom) {
        this.content = content;
        this.classRoom = classRoom;
    }

    public void update(String content) {
        this.content = content;
    }

}
