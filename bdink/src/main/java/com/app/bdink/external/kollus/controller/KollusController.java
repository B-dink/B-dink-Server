package com.app.bdink.external.kollus.controller;

import com.app.bdink.external.kollus.dto.request.CallbackRequest;
import com.app.bdink.external.kollus.dto.request.KollusRequest;
import com.app.bdink.external.kollus.service.KollusService;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/kollus")
@RequiredArgsConstructor
@Tag(name = "Kollus API", description = "Kollus 관련 API들입니다.")
public class KollusController {

    private final KollusService kollusService;
    
    @PostMapping("/userkey")
    @Operation(method = "POST", description = "Kollus 유저 키 생성 API 입니다.")
    public RspTemplate<?> userKeyCreate(@RequestBody KollusRequest kollusRequest) {

        return RspTemplate.success(Success.KOLLUS_UPLOAD_SUCCESS);
    }
    

    @PostMapping("/upload")
    @Operation(method = "POST", description = "Kollus upload callback API 입니다.")
    public RspTemplate<?> uploadCallback(@ModelAttribute CallbackRequest.uploadRequestDTO uploadRequestDTO) {
        log.info("------------------------------------------------------------");
        log.info(String.valueOf(uploadRequestDTO.getContent_provider_key()));
        log.info(String.valueOf(uploadRequestDTO.getFilename()));
        log.info(String.valueOf(uploadRequestDTO.getUpload_file_key()));
        log.info(String.valueOf(uploadRequestDTO.getMedia_content_key()));
        kollusService.uploadCallbackService(uploadRequestDTO);
        log.info("------------------------------------------------------------");
        return RspTemplate.success(Success.KOLLUS_UPLOAD_SUCCESS);
    }

    @PostMapping("/delete")
    @Operation(method = "POST", description = "Kollus delete Callback API 입니다.")
    public RspTemplate<?> deleteCallback(@ModelAttribute CallbackRequest.deleteRequestDTO deleteRequestDTO) {
        log.info("------------------------------------------------------------");
        log.info(String.valueOf(deleteRequestDTO.getContent_provider_key()));
        log.info(String.valueOf(deleteRequestDTO.getFull_filename()));
        log.info(String.valueOf(deleteRequestDTO.getFilename()));
        log.info(String.valueOf(deleteRequestDTO.getUpload_file_key()));
        log.info(String.valueOf(deleteRequestDTO.getMedia_content_key()));
        log.info(String.valueOf(deleteRequestDTO.getChannel_key()));
        log.info(String.valueOf(deleteRequestDTO.getChannel_name()));
        log.info(String.valueOf(deleteRequestDTO.getUpdate_type()));
        kollusService.deleteCallbackService(deleteRequestDTO);
        log.info("------------------------------------------------------------");
        return RspTemplate.success(Success.KOLLUS_DELETE_SUCCESS);
    }

    @PostMapping("/lms")
    @Operation(method = "POST", description = "Kollus LMS Callback API 입니다.")
    public RspTemplate<?> lmsCallback(@ModelAttribute CallbackRequest.playRequestDTO playRequestDTO) {
        log.info("------------------------------------------------------------");
        log.info(String.valueOf(playRequestDTO.getContent_provider_key()));
        log.info(String.valueOf(playRequestDTO.getMedia_content_key()));
        log.info(String.valueOf(playRequestDTO.getMedia_profile_key()));
        log.info(String.valueOf(playRequestDTO.getUser_key()));
        log.info(String.valueOf(playRequestDTO.getLog_type()));
        log.info(String.valueOf(playRequestDTO.getFull_filename()));
        log.info(String.valueOf(playRequestDTO.getPlay_time()));
        log.info(String.valueOf(playRequestDTO.getDuration()));
        kollusService.playCallbackService(playRequestDTO);
        log.info("------------------------------------------------------------");
        return RspTemplate.success(Success.KOLLUS_LMS_SUCCESS);
    }

//    @PostMapping("/lms")
//    @Operation(method = "POST", description = "Kollus LMS Callback API 입니다.")
//    public RspTemplate<?> lmsCallback(@ModelAttribute CallbackRequest.UploadRequestDTO uploadRequestDTO) {
//        return RspTemplate.success(Success.KOLLUS_LMS_SUCCESS);
//    }
}
