package com.app.bdink.external.aws.s3.controller.dto;

public record S3UploadSignedUrlDto(
        String fileName,
        String uploadId,
        int partNumber
) {
}
