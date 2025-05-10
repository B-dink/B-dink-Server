package com.app.bdink.report.entity;

import com.app.bdink.common.entity.BaseTimeEntity;
import com.app.bdink.member.entity.Member;
import com.app.bdink.qna.entity.Question;
import com.app.bdink.report.domain.ReportCase;
import com.app.bdink.report.domain.ReportReason;
import com.app.bdink.review.domain.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Enumerated(EnumType.STRING)
    private ReportCase reportCase;

    private String reportReason;

    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Report(Member member, ReportCase reportCase, String reportReason, Long reportId) {
        this.member = member;
        this.reportCase = reportCase;
        this.reportId = reportId;
        this.reportReason = reportReason;
    }

}
