package com.app.bdink.payment.controller;

import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.util.MemberUtilService;
import com.app.bdink.payment.controller.dto.CancelRequest;
import com.app.bdink.payment.controller.dto.ConfirmRequest;
import com.app.bdink.payment.controller.dto.PaymentResponse;
import com.app.bdink.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "결제 API")
public class PaymentController {

    private final PaymentService paymentService;
    private final MemberUtilService memberUtilService;

    @PostMapping("/confirm")
    @Operation(summary = "결제 승인", description = "토스페이먼츠 결제 승인을 처리합니다")
    public Mono<RspTemplate<PaymentResponse>> confirmPayment(
            Principal principal,
            @RequestBody ConfirmRequest confirmRequest) {
        return paymentService.confirm(memberUtilService.getMemberId(principal), confirmRequest);
    }

    @GetMapping("/{paymentKey}")
    @Operation(summary = "paymentKey로 결제 조회",
            description = "승인된 결제를 paymentKey로 조회합니다. " +
                    "paymentKey는 SDK를 사용해 결제할 때 발급되는 고유한 키값으로 결제가 최종 승인된 후 돌아오는 Payment 객체에 담겨있습니다.")
    public Mono<RspTemplate<PaymentResponse>> getPaymentByPaymentKey(
            @PathVariable String paymentKey) {
        return paymentService.getPaymentByPaymentKey(paymentKey);
    }

    @GetMapping("/orders/{orderId}")
    @Operation(summary = "orderId로 결제 조회",
            description = "승인된 결제를 orderId로 조회합니다. orderId는 SDK로 결제를 요청할 때 직접 생성한 값입니다.")
    public Mono<RspTemplate<PaymentResponse>> getPaymentByOrderId(
            @PathVariable String orderId) {
        return paymentService.getPaymentByOrderId(orderId);
    }

    @PostMapping("/{paymentKey}/cancel")
    @Operation(summary = "결제 취소",
            description = "승인된 결제를 paymentKey로 취소합니다. 취소 이유를 cancelReason에 추가해야 합니다. " +
                    "결제 금액의 일부만 부분 취소하려면 cancelAmount에 취소할 금액을 추가해서 API를 요청합니다. " +
                    "cancelAmount에 값을 넣지 않으면 전액 취소됩니다. cancelAmout에 빈문자열을 넣으면 전액 취소됩니다.")
    public Mono<RspTemplate<PaymentResponse>> cancelPayment(
            @PathVariable String paymentKey, @RequestBody CancelRequest cancelRequest) {
        return paymentService.cancelPayment(paymentKey, cancelRequest);
    }
}