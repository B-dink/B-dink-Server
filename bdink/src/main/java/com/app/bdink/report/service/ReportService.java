package com.app.bdink.report.service;

import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.qna.entity.Question;
import com.app.bdink.qna.service.QuestionService;
import com.app.bdink.report.controller.dto.request.ReportDto;
import com.app.bdink.report.controller.dto.response.ReportResponse;
import com.app.bdink.report.domain.ReportCase;
import com.app.bdink.report.domain.ReportReason;
import com.app.bdink.report.entity.Report;
import com.app.bdink.report.repository.ReportRepository;
import com.app.bdink.review.domain.Review;
import com.app.bdink.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final QuestionService questionService;
    private final MemberService memberService;
    private final MemberUtilService memberUtilService;
    private final ReportRepository reportRepository;
    private final ReviewService reviewService;


    //TODO: 질문 신고 -> 질문을 유저가 신고하면 블록하고, 해당 유저가 그 질문을 볼수없어야함.

    @Transactional
    public String report(Principal principal, Long id, String reportCase, ReportDto dto){
        Member member = memberService.findById(memberUtilService.getMemberId(principal));


        if (ReportCase.QUESTION.name().equals(reportCase)){
            questionService.findById(id);
        }else{
            reviewService.findById(id);
        }

        Report report = Report.builder()
                .reportId(id)
                .reportCase(ReportCase.valueOf(reportCase))
                .reportReason(dto.reportReason())
                .member(member)
                .build();

        report = reportRepository.save(report);
        return report.getId().toString();
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> responseList(Principal principal, ReportCase reportCase){
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        return reportRepository.findAllByMemberAndReportCase(member, reportCase).stream()
                .map(ReportResponse::from)
                .toList();
    }

}
