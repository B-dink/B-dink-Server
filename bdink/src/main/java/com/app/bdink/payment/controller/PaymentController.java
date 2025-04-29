package com.app.bdink.payment.controller;

import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.payment.controller.dto.ConfirmRequest;
import com.app.bdink.payment.domain.PaymentConfirmResponse;
import com.app.bdink.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "결제 API")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    @Operation(summary = "결제 승인", description = "토스페이먼츠 결제 승인을 처리합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "결제 거부"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public Mono<RspTemplate<PaymentConfirmResponse>> confirmPayment(@RequestBody ConfirmRequest confirmRequest) {
        return paymentService.confirm(confirmRequest);
    }
}