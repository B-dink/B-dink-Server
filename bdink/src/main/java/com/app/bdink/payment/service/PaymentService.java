package com.app.bdink.payment.service;

import com.app.bdink.global.exception.Success;
import com.app.bdink.global.exception.model.PaymentFailedException;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.payment.controller.dto.CancelRequest;
import com.app.bdink.payment.controller.dto.ConfirmRequest;
import com.app.bdink.payment.controller.dto.PaymentResponse;
import com.app.bdink.payment.repository.PaymentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String tossUrl = "https://api.tosspayments.com/v1/payments";

    public Mono<RspTemplate<PaymentResponse>> confirm(
            ConfirmRequest confirmRequest) throws PaymentFailedException {
        String authorizations = getBasicAuthHeader();
        WebClient webClient = WebClient.create(tossUrl);
        Mono<PaymentResponse> responseMono = webClient.post()
                .uri("/confirm")
                .header("Authorization", authorizations)
                .header("Content-Type", "application/json")
                .bodyValue(confirmRequest)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        this::handleErrorResponse
                )
                .bodyToMono(PaymentResponse.class);

        return toRspTemplate(responseMono, Success.CREATE_PAYMENT_SUCCESS);
    }

    public Mono<RspTemplate<PaymentResponse>> getPaymentByPaymentKey(String paymentKey) {
        String authorization = getBasicAuthHeader();
        WebClient webClient = WebClient.create(tossUrl);
        Mono<PaymentResponse> responseMono = webClient.get()
                .uri("/{paymentKey}", paymentKey)
                .header("Authorization", authorization)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        this::handleErrorResponse
                )
                .bodyToMono(PaymentResponse.class);

        return toRspTemplate(responseMono, Success.GET_TOSS_PAYMENT_SUCCESS);
    }

    public Mono<RspTemplate<PaymentResponse>> getPaymentByOrderId(String orderId) {
        String authorization = getBasicAuthHeader();
        WebClient webClient = WebClient.create(tossUrl);
        Mono<PaymentResponse> responseMono = webClient.get()
                .uri("/{orderId}", orderId)
                .header("Authorization", authorization)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        this::handleErrorResponse
                )
                .bodyToMono(PaymentResponse.class);

        return toRspTemplate(responseMono, Success.GET_TOSS_PAYMENT_SUCCESS);
    }

    public Mono<RspTemplate<PaymentResponse>> cancelPayment(
            String paymentKey, CancelRequest cancelRequest) {
        String authorization = getBasicAuthHeader();
        WebClient webClient = WebClient.create(tossUrl);
        Mono<PaymentResponse> responseMono = webClient.post()
                .uri("/{paymentId}/cancel")
                .header("Authorization", authorization)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        this::handleErrorResponse
                )
                .bodyToMono(PaymentResponse.class);

        return toRspTemplate(responseMono, Success.CANCEL_TOSS_PAYMENT_SUCCESS);
    }

    private String getBasicAuthHeader() {
        Base64.Encoder encoder = Base64.getEncoder();
        //TODO: 테스트에서 나중에 사업자등록된거로 받기.
        String WIDGET_SECRET_KEY = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
        byte[] encodedBytes = encoder.encode((WIDGET_SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedBytes);
    }

    private Mono<? extends Throwable> handleErrorResponse(ClientResponse response) {
        return response.bodyToMono(String.class)
                .flatMap(errorBody -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode errorJson = objectMapper.readTree(errorBody);
                        String errorCode = errorJson.get("code").asText();
                        String errorMessage = errorJson.get("message").asText();

                        Error appError = Error.fromTossPaymentCode(errorCode);
                        return Mono.error(new PaymentFailedException(appError, errorMessage));
                    } catch (Exception e) {
                        return Mono.error(new PaymentFailedException(
                                Error.FAILED_PARSING_TOSSPAY_ERROR_RESPONSE, e.getMessage()));
                    }
                });
    }

    private <T> Mono<RspTemplate<T>> toRspTemplate(Mono<T> responseMono, Success success) {
        return responseMono.map(response ->
                RspTemplate.success(success, response)
        ).onErrorResume(Mono::error);
    }

    // 추후 구현 예정
    private void savePaymentSuccess(PaymentResponse response) {
    }

    // 추후 구현 예정
    private void savePaymentFailure(ConfirmRequest confirmRequest, String errorCode, String errorMessage) {
    }
}
