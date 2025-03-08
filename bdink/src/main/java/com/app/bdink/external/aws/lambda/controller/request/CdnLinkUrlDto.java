package com.app.bdink.external.aws.lambda.controller.request;

public record CdnLinkUrlDto(
        String assetID,
        String s3Key
) {
}
