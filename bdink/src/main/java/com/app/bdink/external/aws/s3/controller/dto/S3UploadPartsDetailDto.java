package com.app.bdink.external.aws.s3.controller.dto;

public record S3UploadPartsDetailDto(
        int partNumber,
        String AWSEtag
) {
}
