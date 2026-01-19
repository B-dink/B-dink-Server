package com.app.bdink.centerowner.controller;

import com.app.bdink.centerowner.controller.dto.request.CenterOwnerCreateRequest;
import com.app.bdink.centerowner.controller.dto.request.CenterOwnerUpdateRequest;
import com.app.bdink.centerowner.controller.dto.response.CenterOwnerResponse;
import com.app.bdink.centerowner.entity.CenterOwner;
import com.app.bdink.centerowner.service.CenterOwnerService;
import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.entity.Member;
import com.app.bdink.member.service.MemberService;
import com.app.bdink.member.util.MemberUtilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * 센터장 CRUD API 컨트롤러.
 * 인증 주체(Principal) 기준으로 본인 센터장만 수정/삭제 가능하다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/centerOwner")
@Tag(name = "센터장 API", description = "센터장 관련 CRUD API입니다.")
public class CenterOwnerController {

    private final CenterOwnerService centerOwnerService;
    private final MemberService memberService;
    private final MemberUtilService memberUtilService;

    @PostMapping
    @Operation(method = "POST", description = "센터장을 생성합니다.")
    public RspTemplate<?> createCenterOwner(Principal principal,
                                            @RequestBody CenterOwnerCreateRequest request) {
        // 토큰의 주체(memberId)로 센터장을 생성한다.
        Member member = memberService.findById(memberUtilService.getMemberId(principal));
        String centerOwnerId = centerOwnerService.createCenterOwner(member, request);
        return RspTemplate.success(Success.CREATE_CENTER_OWNER_SUCCESS, CreateIdDto.from(centerOwnerId));
    }

    @GetMapping("/{id}")
    @Operation(method = "GET", description = "센터장 정보를 조회합니다.")
    public RspTemplate<?> getCenterOwner(@PathVariable Long id) {
        CenterOwner centerOwner = centerOwnerService.getActiveCenterOwner(id);
        return RspTemplate.success(Success.GET_CENTER_OWNER_SUCCESS, CenterOwnerResponse.from(centerOwner));
    }

    @GetMapping("/center/{centerId}")
    @Operation(method = "GET", description = "센터별 센터장 목록을 조회합니다.")
    public RspTemplate<?> getCenterOwnersByCenter(@PathVariable Long centerId) {
        List<CenterOwnerResponse> responses = centerOwnerService.getActiveCenterOwnersByCenter(centerId).stream()
                .map(CenterOwnerResponse::from)
                .toList();
        return RspTemplate.success(Success.GET_CENTER_OWNER_SUCCESS, responses);
    }

    @PutMapping("/{id}")
    @Operation(method = "PUT", description = "센터장을 수정합니다.")
    public RspTemplate<?> updateCenterOwner(Principal principal,
                                            @PathVariable Long id,
                                            @RequestBody CenterOwnerUpdateRequest request) {
        Long memberId = memberUtilService.getMemberId(principal);
        CenterOwner centerOwner = centerOwnerService.getActiveCenterOwner(id);

        // 본인 센터장만 수정 가능
        if (!centerOwner.getMember().getId().equals(memberId)) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }

        CenterOwnerResponse response = centerOwnerService.updateCenterOwner(id, request);
        return RspTemplate.success(Success.UPDATE_CENTER_OWNER_SUCCESS, response);
    }

    @DeleteMapping("/{id}")
    @Operation(method = "DELETE", description = "센터장을 삭제합니다. soft delete로 진행됩니다.")
    public RspTemplate<?> deleteCenterOwner(Principal principal, @PathVariable Long id) {
        Long memberId = memberUtilService.getMemberId(principal);
        CenterOwner centerOwner = centerOwnerService.getActiveCenterOwner(id);

        // 본인 센터장만 삭제 가능
        if (!centerOwner.getMember().getId().equals(memberId)) {
            throw new CustomException(Error.INVALID_USER_ACCESS, Error.INVALID_USER_ACCESS.getMessage());
        }

        centerOwnerService.deleteCenterOwner(id);
        return RspTemplate.success(Success.DELETE_CENTER_OWNER_SUCCESS, Success.DELETE_CENTER_OWNER_SUCCESS.getMessage());
    }
}
