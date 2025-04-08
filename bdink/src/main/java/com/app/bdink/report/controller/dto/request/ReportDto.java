package com.app.bdink.report.controller.dto.request;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Error;
import com.app.bdink.report.domain.ReportReason;

public record ReportDto(

        String reportReason
) {
    public ReportReason toDomain(){
        for(ReportReason a : ReportReason.values()){
            if (a.equals(ReportReason.valueOf(reportReason))){
                return a;
            }
        }
        throw new CustomException(Error.NOT_FOUND_REPORT_TYPE, Error.NOT_FOUND_REPORT_TYPE.getMessage());
    }
}
