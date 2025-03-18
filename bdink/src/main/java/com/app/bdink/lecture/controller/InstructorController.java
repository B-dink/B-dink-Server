package com.app.bdink.lecture.controller;

import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.common.util.MemberUtilService;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.lecture.controller.dto.InstructorDto;
import com.app.bdink.lecture.controller.dto.request.UpdateInstructorDto;
import com.app.bdink.lecture.controller.dto.response.InstructorInfoDto;
import com.app.bdink.lecture.service.InstructorService;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/instructor")
@Tag(name = "강사 API", description = "강사와 관련된 API들입니다. 강사는 회원가입해 Member 엔티티가 생성된 이후에 생성할 수 있습니다.")
public class InstructorController {

    private final InstructorService instructorService;
    private final MemberService memberService;
    private final MemberUtilService memberUtilService;

    @PostMapping
    @Operation(method = "POST", description = "강사 정보를 생성합니다.")
    public RspTemplate<?> createInstructor(Principal principal, @RequestBody InstructorDto instructorDto){

        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        String instructorId = instructorService.createInstructor(member, instructorDto);

        return RspTemplate.success(Success.CREATE_INSTRUCTOR_SUCCESS, CreateIdDto.from(instructorId));
    }

    @GetMapping
    @Operation(method = "GET", description = "강사 정보를 조회합니다.")
    public RspTemplate<?> getInstructorInfo(Principal principal){
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        InstructorInfoDto instructorInfo = instructorService.getInfo(member);
        return RspTemplate.success(Success.GET_INSTRUCTOR_SUCCESS, instructorInfo);
    }

    @PutMapping
    @Operation(method = "PUT", description = "강사 정보를 수정합니다.")
    public RspTemplate<?> modifyInstructorInfo(Principal principal, @RequestBody UpdateInstructorDto instructorDto){
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        InstructorInfoDto infoDto = instructorService.modifyInstructorInfo(member, instructorDto);
        return RspTemplate.success(Success.UPDATE_INSTRUCTOR_SUCCESS, infoDto);
    }

    @DeleteMapping
    @Operation(method = "DELETE", description = "강사 정보를 삭제합니다. soft delete를 진행합니다. 강사정보를 제거하더라도 자신이 등록한 강의를 보유하고 싶을 수도 있기 때문에")
    public RspTemplate<?> deleteInstructor(Principal principal){
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        instructorService.deleteInstructor(member);
        return RspTemplate.success(Success.DELETE_INSTRUCTOR_SUCCESS, Success.DELETE_INSTRUCTOR_SUCCESS.getMessage());
    }
}
