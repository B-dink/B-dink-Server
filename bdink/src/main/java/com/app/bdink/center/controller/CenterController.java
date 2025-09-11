package com.app.bdink.center.controller;

import com.app.bdink.center.repository.CenterRepository;
import com.app.bdink.center.service.CenterService;
import com.app.bdink.common.util.CreateIdDto;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/center")
@Tag(name = "센터 API", description = "센터와 관련된 API들 입니다.")
public class CenterController {
    private final CenterRepository centerRepository;
    private final CenterService centerService;
    private final MemberService memberService;
    private final MemberUtilService memberUtilService;

    @PostMapping
    @Operation(method = "GET", description = "센터를 생성합니다.")
    public RspTemplate<?> createCenter(String name, String address, String qrToken) {

        //centerService.createCenter(name, address, qrToken);

        return RspTemplate.success(Success.CREATE_CENTER_SUCCESS);
    }

    @GetMapping
    @Operation(method="GET", description = "센터를 조회합니다.")
    public RspTemplate<?> getCenter(){
        return RspTemplate.success(Success.GET_CENTER_SUCCESS);
    }

    @PatchMapping
    @Operation(method = "PATCH", description = "센터 정보를 수정합니다.")
    public RspTemplate<?> updateCenter(String name, String address, String qrToken){
        return RspTemplate.success(Success.UPDATE_CENTER_SUCCESS);
    }

    @DeleteMapping
    @Operation(method = "DELETE", description = "센터 정보를 삭제합니다. soft delete를 진행합니다. 어떤 센터와 계약 체결에 대한 정보를 유지하기 위해")
    RspTemplate<?> deleteCenter(Long id){
        return RspTemplate.success(Success.DELETE_CENTER_SUCCESS);
    }

}