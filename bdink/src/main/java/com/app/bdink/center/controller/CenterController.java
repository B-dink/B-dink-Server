package com.app.bdink.center.controller;

import com.app.bdink.center.controller.dto.request.CenterInfoDto;
import com.app.bdink.center.controller.dto.response.CenterAllListDto;
import com.app.bdink.center.service.CenterService;
import com.app.bdink.common.util.CreateIdDto;
import com.app.bdink.external.aws.service.S3Service;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/center")
@Tag(name = "센터 API", description = "센터와 관련된 API들 입니다.")
public class CenterController {
    private final CenterService centerService;
    private final S3Service s3Service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(method = "GET", description = "센터를 생성합니다.")
    public RspTemplate<?> createCenter(@RequestPart CenterInfoDto centerInfoDto,
                                       @RequestPart(value = "centerImage", required = false) MultipartFile centerImage) {

        String profileImageUrl = null;

        if (centerImage !=null && !centerImage.isEmpty()){
            profileImageUrl = s3Service.uploadImageOrMedia("image/", centerImage);
        }

        String centerId = centerService.saveCenter(centerInfoDto, profileImageUrl);
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

//    @PostMapping("/verify")
//    @Operation(method = "POST", description = "QR코드 검증 api입니다.")
//    public RspTemplate<?> checkCenter(Principal principal, @RequestBody CenterQrDto centerQrDto){
//        Long memberId = memberUtilService.getMemberId(principal);
//        Member member = memberService.findById(memberId);
//        String classRoomId = centerService.verifyQrCode(member, centerQrDto);
//        return RspTemplate.success(Success.CHECK_QR_SUCCESS, CreateIdDto.from(classRoomId));
//    }

}