package com.app.bdink.external.aws.s3.controller.dto;

import java.util.List;

public record S3UploadCompleteDto(
        List<S3UploadPartsDetailDto> parts,
        String fileName,
        String uploadId,
        Long totalSeconds //총 시간


) {
}
