package com.app.bdink.report.controller.dto.request;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.report.domain.ReportReason;

public record ReportDto(

        String reportReason
) {
}
