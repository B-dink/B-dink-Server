package com.app.bdink.center.controller;

import com.app.bdink.center.controller.dto.request.CenterInfoDto;
import com.app.bdink.center.controller.dto.response.CenterAllListDto;
import com.app.bdink.center.service.CenterService;
import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/center")
@Tag(name = "센터 API", description = "센터와 관련된 API들 입니다.")
public class CenterController {
    private final CenterService centerService;

    @PostMapping
    @Operation(method = "GET", description = "센터를 생성합니다.")
    public RspTemplate<?> createCenter(@RequestBody CenterInfoDto centerInfoDto) {

        String centerId = centerService.saveCenter(centerInfoDto);
        return RspTemplate.success(Success.CREATE_CENTER_SUCCESS, CreateIdDto.from(centerId));
    }

    @GetMapping("/{id}")
    @Operation(method="GET", description = "센터를 조회합니다.")
    public RspTemplate<?> getCenter(@PathVariable Long id) {
        return RspTemplate.success(Success.GET_CENTER_SUCCESS, centerService.getCenterInfo(id));
    }

    @PatchMapping("/{id}")
    @Operation(method = "PATCH", description = "센터 정보를 수정합니다.")
    public RspTemplate<?> updateCenter(@PathVariable Long id, @RequestBody CenterInfoDto centerInfoDto) {
        return RspTemplate.success(Success.UPDATE_CENTER_SUCCESS, centerService.updateCenter(id, centerInfoDto));
    }

    @DeleteMapping("/{id}")
    @Operation(method = "DELETE", description = "센터 정보를 삭제합니다. 어떤 센터와 계약 체결에 대한 정보를 유지하기 위해 softdelete로 진행됩니다.")
    RspTemplate<?> deleteCenter(@PathVariable Long id) {
        centerService.deleteCenter(id);
        return RspTemplate.success(Success.DELETE_CENTER_SUCCESS, Success.DELETE_CENTER_SUCCESS.getMessage());
    }

    @GetMapping("/all")
    @Operation(method="GET", description = "모든 센터를 조회합니다.")
    public RspTemplate<?> getAllCenter(){
        List<CenterAllListDto> centerAllListDtos =  centerService.getAllInProgressCenters();
        return RspTemplate.success(Success.GET_ALLCENTER_SUCCESS, centerAllListDtos);
    }

}