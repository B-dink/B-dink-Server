package com.app.bdink.payment.service;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.exception.model.PaymentFailedException;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.payment.controller.dto.ConfirmRequest;
import com.app.bdink.payment.repository.PaymentRepository;
import com.app.bdink.payment.domain.PaymentConfirmResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.View;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.app.bdink.global.exception.Error;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final View error;
    private String WIDGET_SECRET = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6"; //TODO: 테스트에서 나중에 사업자등록된거로 받기.
    private final String tossUrl = "https://api.tosspayments.com/v1/payments";

    public Mono<RspTemplate<PaymentConfirmResponse>> confirm(
            ConfirmRequest confirmRequest
    ) throws PaymentFailedException {
        String authorizations = getBasicAuthHeader();
        WebClient webClient = WebClient.create(tossUrl);
        Mono<PaymentConfirmResponse> responseMono = webClient.post()
                .uri("/confirm")
                .header("Authorization", authorizations)
                .header("Content-Type", "application/json")
                .bodyValue(confirmRequest)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> handleErrorResponse(response, confirmRequest)
                )
                .bodyToMono(PaymentConfirmResponse.class)
                .doOnNext(response -> {
                    if (response != null) {
                        savePaymentSuccess(response);
                    }
                });

        return toRspTemplate(responseMono);
    }

    private String getBasicAuthHeader() {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((WIDGET_SECRET + ":").getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedBytes);
    }

    private Mono<? extends Throwable> handleErrorResponse(
            ClientResponse response, ConfirmRequest confirmRequest
    ) throws RuntimeException {
        return response.bodyToMono(String.class)
                .flatMap(errorBody -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode errorJson = objectMapper.readTree(errorBody);
                        String errorCode = errorJson.get("code").asText();
                        String errorMessage = errorJson.get("message").asText();

                        // 결제 실패 정보 저장
                        savePaymentFailure(confirmRequest, errorCode, errorMessage);

                        // 에러 코드에 기반한 적절한 예외 생성
                        Error appError = Error.fromTossPaymentCode(errorCode);
                        return Mono.error(new PaymentFailedException(appError, errorMessage));
                    } catch (Exception e) {
                        return Mono.error(new PaymentFailedException(Error.FAILED_PARSING_TOSSPAY_ERROR_RESPONSE, e.getMessage()));
                    }
                });
    }

    private void savePaymentSuccess(PaymentConfirmResponse response) {
    }

    private void savePaymentFailure(ConfirmRequest confirmRequest, String errorCode, String errorMessage) {
    }

    private Mono<RspTemplate<PaymentConfirmResponse>> toRspTemplate(Mono<PaymentConfirmResponse> responseMono) {
        return responseMono.map(response ->
                RspTemplate.success(Success.CREATE_PAYMENT_SUCCESS, response)
        ).onErrorResume(Mono::error);
    }
}
