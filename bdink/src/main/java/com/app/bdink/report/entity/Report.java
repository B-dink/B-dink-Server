package com.app.bdink.report.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.member.entity.Member;
import com.app.bdink.qna.entity.Question;
import com.app.bdink.review.domain.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Getter
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    public Report(Question question, Member member, Review review) {
        this.question = question;
        this.member = member;
        this.review = review;
    }

}
