package com.app.bdink.sugang.controller;

import com.app.bdink.common.util.MemberUtilService;
import com.app.bdink.external.aws.lambda.domain.Media;
import com.app.bdink.external.aws.lambda.service.MediaService;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.lecture.entity.Lecture;
import com.app.bdink.lecture.service.LectureService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.sugang.controller.dto.response.SugangInfoDto;
import com.app.bdink.sugang.entity.Sugang;
import com.app.bdink.sugang.service.SugangService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sugang")
public class SugangController {
    private final SugangService sugangService;
    private final LectureService lectureService;
    private final MemberUtilService memberUtilService;
    private final MemberService memberService;
    private final MediaService mediaService;

    @GetMapping
    public  RspTemplate<SugangInfoDto> getSugangInfo(Principal principal, @RequestParam Long lectureId){
        Long memberId = memberUtilService.getMemberId(principal);
        Member member = memberService.findById(memberId);

        return RspTemplate.success(Success.GET_SUGANG_SUCCESS, sugangService.getSugangLecture(
                member,
                lectureService.findById(lectureId)
        ));
    }

    @PostMapping
    @Operation(method = "POST", description = "수강을 시작합니다. 수강 진행률도 0으로 생성.")
    public RspTemplate<SugangInfoDto> startSugang(Principal principal, @RequestParam Long lectureId){
        Long memberId = memberUtilService.getMemberId(principal);
        Member member = memberService.findById(memberId);
        Media media =  mediaService.findByLectureId(lectureId);
        Lecture lecture = lectureService.findById(lectureId);
        SugangInfoDto info = sugangService.createSugang(media, member, lecture);
        return RspTemplate.success(Success.CREATE_SUGANG_SUCCESS, info);
    }

//    @PatchMapping
//    @Operation(method = "POST", description = "수강률을 10초마다 업데이트합니다.")
//    public RspTemplate<SugangInfoDto> updateSugangProgress(Principal principal, @RequestParam Long lectureId){
//
//        return RspTemplate.success(Success.CREATE_SUGANG_SUCCESS, info);
//    }
}
