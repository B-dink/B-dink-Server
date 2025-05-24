package com.app.bdink.version.controller;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.version.controller.dto.*;
import com.app.bdink.version.entity.Version;
import com.app.bdink.version.service.VersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/version")
@Tag(name = "버전 API", description = "앱 버전과 관련된 API입니다.")
public class VersionController {

    private final VersionService versionService;

    @PostMapping
    @Operation(method = "POST", description = "새로운 앱 버전 정보를 등록합니다. 클라이언트 앱 버전에 대한 기록을 남기는 용도입니다.")
    public RspTemplate<PostVersionResponse> postVersion(
            @Valid @RequestBody VersionRequest request) {
        Long id = versionService.addVersion(request.toEntity());
        PostVersionResponse response = new PostVersionResponse(id);
        return RspTemplate.success(Success.CREATE_VERSION_SUCCESS, response);
    }

    @GetMapping("{id}")
    @Operation(method = "GET", description = "ID를 통해 특정 버전 정보를 조회합니다.")
    public RspTemplate<VersionResponse> getVersion(@PathVariable Long id) {
        Version version = versionService.getVersion(id);
        VersionResponse response = VersionResponse.from(version);
        return RspTemplate.success(Success.GET_VERSION_SUCCESS, response);
    }

    @GetMapping("/check-update")
    @Operation(method = "GET", description = "유저의 현재 버전과 플랫폼을 기준으로 업데이트 필요 여부와 강제 업데이트 정보를 확인합니다.")
    public RspTemplate<UpdateCheckResponse> checkUpdate(
            @Valid @ModelAttribute CheckUpdateRequiredRequest request) {
        Boolean isUpdateRequired = versionService.isUpdateRequired(request.version(), request.platform());
        String version = versionService.getLatestVersion(request.platform()).getVersion();
        CheckUpdateRequiredResponse checkUpdateRequiredResponse = new CheckUpdateRequiredResponse(isUpdateRequired, version);

        ForceUpdateInfo forceUpdateInfo = versionService.checkForceUpdateInfo(request.version(), request.platform());

        UpdateCheckResponse response = UpdateCheckResponse.from(checkUpdateRequiredResponse, forceUpdateInfo);

        return RspTemplate.success(Success.GET_UPDATE_CHECK_SUCCESS, response);
    }
}
