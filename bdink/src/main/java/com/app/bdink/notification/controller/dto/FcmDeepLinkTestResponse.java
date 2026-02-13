package com.app.bdink.notification.controller.dto;

import java.util.List;

public record FcmDeepLinkTestResponse(
        List<FcmDeepLinkItem> items
) {
    public record FcmDeepLinkItem(
            String linkType,
            String linkId,
            String title,
            String body
    ) {}
}
