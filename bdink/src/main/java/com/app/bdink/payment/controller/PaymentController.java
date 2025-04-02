package com.app.bdink.payment.controller;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.payment.controller.dto.ConfirmRequest;
import com.app.bdink.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "결제 API")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public RspTemplate<?> confirm(@RequestBody ConfirmRequest confirmRequest) {

        return RspTemplate.success(Success.CREATE_PAYMENT_SUCCESS, paymentService.confirm(confirmRequest));
    }
}
