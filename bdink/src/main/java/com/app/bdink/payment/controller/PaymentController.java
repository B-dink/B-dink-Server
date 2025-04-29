package com.app.bdink.payment.controller;

import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.payment.controller.dto.ConfirmRequest;
import com.app.bdink.payment.controller.dto.ConfirmResponse;
import com.app.bdink.payment.domain.PaymentConfirmResponse;
import com.app.bdink.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "결제 API")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    @Operation(summary = "결제 승인", description = "토스페이먼츠 결제 승인을 처리합니다")
    public Mono<RspTemplate<ConfirmResponse>> confirmPayment(
            @RequestBody ConfirmRequest confirmRequest
    ) {
        return paymentService.confirm(confirmRequest);
    }

    @GetMapping("/{paymentKey}")
    @Operation(summary = "paymentKey로 결제 조회",
            description = "승인된 결제를 paymentKey로 조회합니다. " +
                    "paymentKey는 SDK를 사용해 결제할 때 발급되는 고유한 키값으로 결제가 최종 승인된 후 돌아오는 Payment 객체에 담겨있습니다.")
    public Mono<RspTemplate<PaymentConfirmResponse>> getPayment(
            @PathVariable String paymentKey
    ) {
        return paymentService.getPayment(paymentKey);
    }
}