package com.app.bdink.message.service;

import com.app.bdink.message.controller.dto.AlimTalkRequest;
import com.app.bdink.message.controller.dto.AlimTalkText;
import com.app.bdink.message.controller.dto.TokenRequest;
import com.app.bdink.message.controller.dto.TokenResponse;
import com.app.bdink.message.entity.AlimtalkToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.global.exception.Success;
import com.app.bdink.global.template.RspTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAlimtalkServiceImpl implements KakaoAlimtalkService {

    @Value("${surem.user-code}")
    private String userCode;

    @Value("${surem.secret-key}")
    private String secretKey;

    @Value("${surem.base-url}")
    private String baseUrl;

    @Value("${surem.template-code}")
    private String templateCode;

    @Value("${surem.sender-key}")
    private String senderKey;

    @Value("${surem.req-phone")
    private String BdinkPhoneNumber;

    private final KakaoAlimtalkDataService KakaoAlimtalkDataService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<RspTemplate<TokenResponse>> getAlimTalkToken() throws CustomException {
        log.info("=== 슈어엠 토큰 발급 요청 시작 ===");
        log.info("요청 시간: {}", LocalDateTime.now());
        log.info("요청 URL: {}/api/v1/auth/token", baseUrl);

        TokenRequest tokenRequest = new TokenRequest(userCode, secretKey);

        try {
            String requestJson = objectMapper.writeValueAsString(tokenRequest);
            String maskedRequest = requestJson.replaceAll("\"secretKey\"\\s*:\\s*\"[^\"]*\"", "\"secretKey\":\"***MASKED***\"");
            log.info("요청 Body: {}", maskedRequest);
        } catch (Exception e) {
            log.warn("요청 Body 로깅 실패: {}", e.getMessage());
        }

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .filter(logRequest("토큰발급"))
                .filter(logResponse("토큰발급"))
                .build();

        Mono<TokenResponse> responseMono = webClient.post()
                .uri("/api/v1/auth/token")
                .header("Content-Type", "application/json")
                .bodyValue(tokenRequest)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> handleErrorResponse(response, "토큰발급")
                )
                .bodyToMono(TokenResponse.class)
                .doOnSuccess(response -> {
                    log.info("=== 토큰 발급 성공 ===");
                    log.info("응답 코드: {}", response.code());
                    log.info("완료 시간: {}", LocalDateTime.now());
                })
                .doOnError(error -> {
                    log.error("=== 토큰 발급 실패 ===");
                    log.error("에러 타입: {}", error.getClass().getSimpleName());
                    log.error("에러 메시지: {}", error.getMessage());
                    log.error("실패 시간: {}", LocalDateTime.now());
                });

        return toRspTemplate(responseMono, Success.CREATE_ALIMETALK_TOKEN_SUCCESS);
    }

    @Override
    public Mono<RspTemplate<String>> sendAlimTalk(String phoneNumber, AlimTalkText alimTalkText) {
        log.info("=== 알림톡 발송 요청 시작 ===");
        log.info("요청 시간: {}", LocalDateTime.now());
        log.info("수신번호: {}", phoneNumber);

        return Mono.fromCallable(() -> {
                    AlimtalkToken token = KakaoAlimtalkDataService.getAlimtalkToken();
                    return token.getToken();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(accessToken -> log.info("토큰 조회 성공 - 토큰 길이: {}", accessToken.length()))
                .flatMap(accessToken -> {
                    String text = generateAlimTalkText(alimTalkText);
                    AlimTalkRequest alimTalkRequest = createAlimTalkRequest(phoneNumber, text);
                    logRequestBody(alimTalkRequest);
                    return sendAlimTalkRequest(accessToken, alimTalkRequest);
                })
                .doOnError(error -> {
                    log.error("=== 알림톡 발송 실패 ===");
                    log.error("에러 타입: {}", error.getClass().getSimpleName());
                    log.error("에러 메시지: {}", error.getMessage());
                    log.error("실패 시간: {}", LocalDateTime.now());
                });
    }

    private String generateAlimTalkText(AlimTalkText alimTalkText) {
        return String.format("""
        %s님, 축하드립니다!
        귀하의 강의가 아래와 같이 판매되었습니다.

        판매 강의: %s
        판매 일시: %s
        누적 판매량: %s개

        지속적인 인기와 관심에 감사드립니다!
                더 좋은 강의 운영을 응원합니다.""",
                alimTalkText.getName(), alimTalkText.getCourse(), alimTalkText.getDate(), alimTalkText.getCount());
    }

    private AlimTalkRequest createAlimTalkRequest(String phoneNumber, String text) {
        log.info("알림톡 요청 객체 생성:");
        log.info("  - 메시지 타입: AT");
        log.info("  - 템플릿 코드: {}", templateCode);
        log.info("  - 수신번호: {}", phoneNumber);
        log.info("  - 메시지 내용: {}", text);

        return new AlimTalkRequest("AT", senderKey, templateCode, phoneNumber,
                BdinkPhoneNumber, "안녕하세요 비딩크입니다.", "강의가 판매 되었습니다.",
                text);
    }

    private Mono<RspTemplate<String>> sendAlimTalkRequest(String accessToken, AlimTalkRequest alimTalkRequest) {
        log.info("요청 URL: {}/api/v1/send/alimtalk", baseUrl);
        log.info("Authorization Header: Bearer {}", accessToken);

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .filter(logRequest("알림톡발송"))
                .filter(logResponse("알림톡발송"))
                .build();

        Mono<String> responseMono = webClient.post()
                .uri("/api/v1/send/alimtalk")
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .bodyValue(alimTalkRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> handleErrorResponse(response, "알림톡발송"))
                .bodyToMono(String.class)
                .doOnSuccess(response -> {
                    log.info("=== 알림톡 발송 성공 ===");
                    log.info("응답 내용: {}", response);
                    log.info("완료 시간: {}", LocalDateTime.now());
                });

        return toRspTemplate(responseMono, Success.SEND_ALIMTALK_SUCCESS);
    }

    private void logRequestBody(AlimTalkRequest alimTalkRequest) {
        try {
            String requestJson = objectMapper.writeValueAsString(alimTalkRequest);
            log.info("알림톡 요청 Body: {}", requestJson);
        } catch (Exception e) {
            log.warn("알림톡 요청 Body 로깅 실패: {}", e.getMessage());
        }
    }

    /**
     * 요청 로깅 필터
     */
    private ExchangeFilterFunction logRequest(String apiType) {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("[{}] HTTP 요청 상세정보:", apiType);
            log.info("  - Method: {}", clientRequest.method());
            log.info("  - URL: {}", clientRequest.url());
            log.info("  - Headers: {}", clientRequest.headers());

            return Mono.just(clientRequest);
        });
    }

    /**
     * 응답 로깅 필터
     */
    private ExchangeFilterFunction logResponse(String apiType) {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("[{}] HTTP 응답 상세정보:", apiType);
            log.info("  - Status Code: {}", clientResponse.statusCode());
            log.info("  - Headers: {}", clientResponse.headers().asHttpHeaders());

            // 응답 본문 로깅 (body를 소비하지 않도록 주의)
            if (clientResponse.statusCode().isError()) {
                log.warn("  - 에러 응답 감지됨");
            }

            return Mono.just(clientResponse);
        });
    }

    private Mono<? extends Throwable> handleErrorResponse(ClientResponse response, String apiType) {
        log.error("[{}] API 에러 응답 처리 시작", apiType);
        log.error("  - HTTP Status: {}", response.statusCode());
        log.error("  - Response Headers: {}", response.headers().asHttpHeaders());

        return response.bodyToMono(String.class)
                .doOnNext(errorBody -> log.error("  - Error Response Body: {}", errorBody))
                .flatMap(errorBody -> {
                    try {
                        JsonNode errorJson = objectMapper.readTree(errorBody);
                        String errorCode = errorJson.has("code") ? errorJson.get("code").asText() : "UNKNOWN";
                        String errorMessage = errorJson.has("message") ? errorJson.get("message").asText() : "알 수 없는 오류";

                        log.error("[{}] 슈어엠 API 에러 상세:", apiType);
                        log.error("  - 에러 코드: {}", errorCode);
                        log.error("  - 에러 메시지: {}", errorMessage);
                        log.error("  - 원본 응답: {}", errorBody);

                        // 에러 코드별 상세 로깅
                        logDetailedError(errorCode, apiType);

                        return Mono.error(new RuntimeException(String.format("[%s] 슈어엠 API 오류: %s (%s)", apiType, errorMessage, errorCode)));
                    } catch (Exception e) {
                        log.error("[{}] 에러 응답 파싱 실패:", apiType);
                        log.error("  - 파싱 에러: {}", e.getMessage());
                        log.error("  - 원본 응답: {}", errorBody);
                        return Mono.error(new RuntimeException(String.format("[%s] API 응답 파싱 실패", apiType), e));
                    }
                });
    }

    /**
     * 에러 코드별 상세 로깅
     */
    private void logDetailedError(String errorCode, String apiType) {
        log.error("[{}] 에러 코드 분석:", apiType);
        switch (errorCode) {
            case "INVALID_USER_CODE":
                log.error("  → 사용자 코드가 잘못되었습니다. 현재 사용 중인 userCode: {}", userCode);
                break;
            case "INVALID_SECRET_KEY":
                log.error("  → 시크릿 키가 잘못되었습니다. secretKey 길이: {}", secretKey.length());
                break;
            case "TOKEN_EXPIRED":
                log.error("  → 토큰이 만료되었습니다. 새로운 토큰 발급이 필요합니다.");
                break;
            case "INSUFFICIENT_BALANCE":
                log.error("  → 잔액이 부족합니다.");
                break;
            case "INVALID_PHONE_NUMBER":
                log.error("  → 전화번호 형식이 잘못되었습니다.");
                break;
            case "TEMPLATE_NOT_FOUND":
                log.error("  → 템플릿을 찾을 수 없습니다. 현재 템플릿 코드: {}", templateCode);
                break;
            default:
                log.error("  → 알 수 없는 에러입니다. 슈어엠 문서를 확인해주세요.");
                break;
        }
    }

    private <T> Mono<RspTemplate<T>> toRspTemplate(Mono<T> responseMono, Success success) {
        return responseMono.map(response -> {
            log.info("RspTemplate 생성 성공: {}", success);
            return RspTemplate.success(success, response);
        }).onErrorResume(error -> {
            log.error("RspTemplate 생성 중 에러 발생: {}", error.getMessage());
            return Mono.error(error);
        });
    }
}