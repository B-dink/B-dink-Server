package com.app.bdink.payment.toss.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.exception.model.PaymentFailedException;
import com.app.bdink.global.template.RspTemplate;
import com.app.bdink.member.exception.NotFoundMemberException;
import com.app.bdink.payment.toss.controller.dto.CancelRequest;
import com.app.bdink.payment.toss.controller.dto.ConfirmRequest;
import com.app.bdink.payment.toss.controller.dto.PaymentResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.app.bdink.global.exception.Error;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class PaymentService {

    //TODO: 테스트에서 나중에 사업자등록된거로 받기.
    @Value("${toss.WIDGET_SECRET_KEY}")
    private String WIDGET_SECRET_KEY;
    private final String tossUrl = "https://api.tosspayments.com/v1/payments";

    private final TransactionalPaymentService transactionalPaymentService;

    public Mono<RspTemplate<PaymentResponse>> confirm(Long memberId, ConfirmRequest confirmRequest) throws CustomException {
        if (memberId == null) {
            return Mono.error(new NotFoundMemberException(Error.NOT_FOUND_USER_EXCEPTION, "해당 멤버를 찾지 못했습니다."));
        }

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
                .bodyToMono(PaymentResponse.class)
                .flatMap(response ->
                        Mono.fromCallable(() ->
                                        transactionalPaymentService.savePaymentTransactional(memberId, response)
                                )
                                .subscribeOn(Schedulers.boundedElastic())
                                .thenReturn(response)
                )
                .onErrorResume(ex -> Mono.error(new PaymentFailedException(Error.PAYMENT_SAVE_FAILED, ex.getMessage())));

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
                ).bodyToMono(PaymentResponse.class);

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
                ).bodyToMono(PaymentResponse.class);

        return toRspTemplate(responseMono, Success.GET_TOSS_PAYMENT_SUCCESS);
    }

    public Mono<RspTemplate<PaymentResponse>> cancelPayment(String paymentKey, CancelRequest cancelRequest) {
        String authorization = getBasicAuthHeader();
        WebClient webClient = WebClient.create(tossUrl);
        Mono<PaymentResponse> responseMono = webClient.post()
                .uri("/{paymentKey}/cancel", paymentKey)
                .header("Authorization", authorization)
                .bodyValue(cancelRequest)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        this::handleErrorResponse
                ).bodyToMono(PaymentResponse.class);

        return toRspTemplate(responseMono, Success.CANCEL_TOSS_PAYMENT_SUCCESS);
    }

    private String getBasicAuthHeader() {
        Base64.Encoder encoder = Base64.getEncoder();
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
}
