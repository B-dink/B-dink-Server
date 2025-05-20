package com.app.bdink.sugang.controller;

import com.app.bdink.classroom.adapter.out.persistence.entity.ClassRoomEntity;
import com.app.bdink.classroom.service.ClassRoomService;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.sugang.controller.dto.SugangStatus;
import com.app.bdink.sugang.controller.dto.response.SugangClassRoomInfo;
import com.app.bdink.sugang.controller.dto.response.SugangInfoDto;
import com.app.bdink.sugang.service.SugangService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sugang")
@Tag(name = "수강 API", description = "수강과 관련된 API들입니다. 결제 성공 후 수강 api를 불러주세요.")
public class SugangController {
    private final SugangService sugangService;
    private final MemberUtilService memberUtilService;
    private final MemberService memberService;
    private final ClassRoomService classRoomService;

    @GetMapping
    public RspTemplate<List<SugangInfoDto>> getSugangInfo(Principal principal) {
        Long memberId = memberUtilService.getMemberId(principal);
        Member member = memberService.findById(memberId);

        return RspTemplate.success(Success.GET_SUGANG_SUCCESS, sugangService.getSugangLecture(member));
    }

    @PostMapping
    @Operation(method = "POST", description = "수강을 시작합니다. 클래스룸에 대한 수강입니다.")
    public RspTemplate<SugangInfoDto> startSugang(Principal principal, @RequestParam Long classRoomId) {
        Long memberId = memberUtilService.getMemberId(principal);
        Member member = memberService.findById(memberId);

        ClassRoomEntity classRoomEntity = classRoomService.findById(classRoomId);

        SugangInfoDto info = sugangService.createSugang(classRoomEntity, member, SugangStatus.PAYMENT_COMPLETED);
        return RspTemplate.success(Success.CREATE_SUGANG_SUCCESS, info);
    }

    @GetMapping("/all")
    @Operation(method = "GET", description = "결제 완료된 수강신청 클래스룸 목록입니다. 진행률까지 포함되어있습니다.")
    public RspTemplate<List<SugangClassRoomInfo>> getSugangClassRoom(Principal principal) {
        Long memberId = memberUtilService.getMemberId(principal);
        Member member = memberService.findById(memberId);
        List<SugangClassRoomInfo> sugangClassRoomInfo = sugangService.getSugangClassRoomInfo(member);

        return RspTemplate.success(Success.GET_SUGANG_CLASSROOM_SUCCESS, sugangClassRoomInfo.isEmpty() ? null : sugangClassRoomInfo);
    }
}
