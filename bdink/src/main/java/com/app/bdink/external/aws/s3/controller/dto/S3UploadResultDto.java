package com.app.bdink.external.aws.s3.controller.dto;

import lombok.Builder;

@Builder
public record S3UploadResultDto(
        String name,
        String url
) {
}
