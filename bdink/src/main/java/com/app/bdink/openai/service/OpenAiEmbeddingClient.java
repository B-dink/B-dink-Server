package com.app.bdink.openai.service;

import com.app.bdink.openai.config.OpenAiConfig;
import com.app.bdink.openai.dto.request.OpenAiEmbeddingRequest;
import com.app.bdink.openai.dto.response.OpenAiEmbeddingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAiEmbeddingClient {
    private final WebClient openAiWebClient;
    private final OpenAiConfig openAiConfig;

    public List<Double> embedText(String text) {
        try {
            OpenAiEmbeddingRequest request = new OpenAiEmbeddingRequest(
                    openAiConfig.getEmbeddingModel(),
                    text
            );

            OpenAiEmbeddingResponse response = openAiWebClient.post()
                    .uri("/embeddings")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(OpenAiEmbeddingResponse.class)
                    .block();

            if (response == null || response.data() == null || response.data().isEmpty()) {
                log.warn("OpenAI embedding 응답이 비어있음");
                return null;
            }

            List<Double> vector = response.data().get(0).embedding();
            if (vector == null || vector.isEmpty()) {
                log.warn("OpenAI embedding 벡터가 비어있음");
                return null;
            }

            return vector;
        } catch (Exception e) {
            log.warn("OpenAI embedding 요청 실패", e);
            return null;
        }
    }
}
