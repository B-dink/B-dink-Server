package com.app.bdink.report.controller.dto.response;

import com.app.bdink.report.domain.ReportCase;
import com.app.bdink.report.entity.Report;

public record ReportResponse(
        Long reportId,
        String reportCase,
        String reportReason
) {
    public static ReportResponse from(final Report report){
        return new ReportResponse(
                report.getReportId(),
                report.getReportCase().name(),
                report.getReportReason().name()
        );
    }
}
