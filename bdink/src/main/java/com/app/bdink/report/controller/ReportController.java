package com.app.bdink.report.controller;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.report.controller.dto.request.ReportDto;
import com.app.bdink.report.controller.dto.response.ReportResponse;
import com.app.bdink.report.domain.ReportCase;
import com.app.bdink.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    @Operation(
            description = "reportCase에는 REVIEW 또는 QUESTION 중에 하나를 넣어야 합니다."
    )
    public RspTemplate<?> report(Principal principal,
                                 @RequestParam String reportCase,
                                 @RequestParam Long reportId,
                                 @RequestBody ReportDto reportDto){

        String id = reportService.report(principal, reportId, reportCase, reportDto);
        return RspTemplate.success(Success.REPORT_SUCCESS, id);
    }

    @GetMapping
    public RspTemplate<List<ReportResponse>> getReportedList(Principal principal,
                                                             @RequestParam ReportCase reportCase){
        List<ReportResponse> response = reportService.responseList(principal, reportCase);
        return RspTemplate.success(Success.GET_REPORT_LIST_SUCCESS, response);
    }

}
