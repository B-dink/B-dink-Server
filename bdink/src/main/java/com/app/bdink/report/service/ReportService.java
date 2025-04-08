package com.app.bdink.report.service;

import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.qna.entity.Question;
import com.app.bdink.qna.service.QuestionService;
import com.app.bdink.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final QuestionService questionService;
    private final MemberService memberService;
    private final MemberUtilService memberUtilService;
    private final ReviewService reviewService;

    //TODO: 질문 신고 -> 질문을 유저가 신고하면 블록하고, 해당 유저가 그 질문을 볼수없어야함.
    public void reportQuestion(Principal principal, Long id){
        Question question = questionService.findById(id);
        Member member = memberService.findById(memberUtilService.getMemberId(principal));

    }
}
