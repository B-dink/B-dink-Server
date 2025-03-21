package com.app.bdink.external.aws.s3.controller;

import com.app.bdink.external.aws.s3.controller.dto.S3UploadDto;
import com.app.bdink.external.aws.s3.controller.dto.S3UploadInitiateDto;
import com.app.bdink.external.aws.s3.controller.dto.S3UploadSignedUrlDto;
import com.app.bdink.external.aws.service.S3MultipartService;
import com.app.bdink.external.aws.s3.controller.dto.S3UploadCompleteDto;
import com.app.bdink.external.aws.s3.controller.dto.S3UploadResultDto;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/multipart")
@Tag(name = "비디오 API", description = "비디오와 관련된 API들입니다. 비디오는 다음과 같은 플로우를 가지고 있습니다.")
public class S3MultipartController {
    private final S3MultipartService s3MultipartService;

    @PostMapping("/initiate-upload")
    public RspTemplate<S3UploadDto> initiateUpload(@RequestBody S3UploadInitiateDto s3UploadInitiateDto){
        return RspTemplate.success(Success.CREATE_PRESIGNURL_SUCCESS, s3MultipartService.initiateUpload(s3UploadInitiateDto.fileName()));
    }

    @PostMapping("/upload-signed-url")
    public RspTemplate<?> getUploadSignedUrl(@RequestBody S3UploadSignedUrlDto s3UploadSignedUrlDto){
        return RspTemplate.success(Success.CREATE_PRESIGNURL_SUCCESS, s3MultipartService.getUploadSignedUrl(s3UploadSignedUrlDto));
    }

    @PostMapping("/complete-upload")
    public RspTemplate<S3UploadResultDto> completeUpload(@RequestBody S3UploadCompleteDto s3UploadCompleteDto){
        return RspTemplate.success(Success.UPLOAD_SUCCESS, s3MultipartService.completeUpload(s3UploadCompleteDto));
    }

}
