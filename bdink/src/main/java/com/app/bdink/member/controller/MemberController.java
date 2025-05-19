package com.app.bdink.member.controller;

import com.app.bdink.external.aws.service.S3Service;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.controller.dto.request.MemberMarketingDto;
import com.app.bdink.member.controller.dto.request.MemberPhoneUpdateRequestDto;
import com.app.bdink.member.controller.dto.request.MemberUpdateNameDto;
import com.app.bdink.member.controller.dto.response.OperationType;
import com.app.bdink.member.controller.dto.response.PatchResponseDto;
import com.app.bdink.member.controller.dto.response.SocialTypeDto;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;
    private final S3Service s3Service;

    @PatchMapping("/phone")
    @Operation(method = "PATCH", description = "핸드폰 번호 수정")
    public RspTemplate<PatchResponseDto> updatePhone(Principal principal,
                                                     @RequestBody @Valid MemberPhoneUpdateRequestDto memberPhoneUpdateRequestDto) {
        memberService.updatePhoneNumber(Long.parseLong(principal.getName()), memberPhoneUpdateRequestDto);
        return RspTemplate.success(Success.UPDATE_PHONE_SUCCESS, PatchResponseDto.from(Long.parseLong(principal.getName()), OperationType.PHONE_UPDATE));
    }

    @PatchMapping("/marketing")
    @Operation(method = "PATCH", description = "마케팅 수신여부 수정")
    public RspTemplate<?> updateMarketing(Principal principal,
                                           @RequestBody MemberMarketingDto memberMarketingDto) {
        Member member = memberService.findById(Long.parseLong(principal.getName()));
        memberService.updateMarketing(member, memberMarketingDto);
        return RspTemplate.success(Success.UPDATE_MAKETING_SUCCESS, PatchResponseDto.from(Long.parseLong(principal.getName()), OperationType.MARKETING_UPDATE));
    }

//    @PatchMapping("/password/reset")
//    @Deprecated
//    @Operation(method = "PATCH", description = "비밀번호 재설정")
//    public RspTemplate<?> resetPassword(Principal principal,
//                                          @RequestBody PasswordValidDto passwordDto) {
//        Member member = memberService.findById(Long.parseLong(principal.getName()));
//        memberService.updatePassword(member, passwordDto);
//        return RspTemplate.success(Success.UPDATE_PASSWORD_SUCCESS, Success.UPDATE_PASSWORD_SUCCESS.getMessage());
//    }

    @GetMapping("/social-type")
    @Operation(method = "GET", description = "어떤 로그인으로 하던 유저인지 확인")
    public RspTemplate<SocialTypeDto> getSocialType(Principal principal){
        Member member = memberService.findById(Long.parseLong(principal.getName()));
        return RspTemplate.success(Success.GET_SOCIAL_TYPE_SUCCESS, SocialTypeDto.from(member.getSocialType()));
    }


//    @PatchMapping("/restore-id")
//    public RspTemplate<?> restoreId(Principal principal,
//                                         @RequestBody PasswordValidDto passwordDto) {
//        Member member = memberService.findById(Long.parseLong(principal.getName()));
//        memberService.updatePassword(member, passwordDto);
//        return RspTemplate.success(Success.UPDATE_MAKETING_SUCCESS, Success.UPDATE_MAKETING_SUCCESS);
//    }

    @PatchMapping("/name")
    public RspTemplate<?> updateName(Principal principal, @Valid @RequestBody MemberUpdateNameDto memberUpdateNameDto) {
        Member member = memberService.findById(Long.parseLong(principal.getName()));
        memberService.updateName(member, memberUpdateNameDto);
        return RspTemplate.success(Success.UPDATE_NAME_SUCCESS, PatchResponseDto.from(Long.parseLong(principal.getName()), OperationType.NAME_UPDATE));
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/picture")
    public RspTemplate<?> updateProfile(Principal principal,
            @RequestPart(value = "profile") MultipartFile img) {
        Member member = memberService.findById(Long.parseLong(principal.getName()));
        memberService.updateProfile(member, s3Service.uploadImageOrMedia("image/", img));
        return RspTemplate.success(Success.UPDATE_PICTUREURL_SUCCESS, PatchResponseDto.from(Long.parseLong(principal.getName()), OperationType.PROFILE_PICTURE_UPDATE));
    }
}