package com.app.bdink.openai.service;

import com.app.bdink.global.exception.CustomException;
import com.app.bdink.openai.config.OpenAiConfig;
import com.app.bdink.openai.dto.OpenAiMessage;
import com.app.bdink.global.exception.Error;
import com.app.bdink.openai.dto.request.OpenAiChatRequest;
import com.app.bdink.openai.dto.response.OpenAiChatResponse;
import com.app.bdink.openai.parser.AiParsedExerciseDto;
import com.app.bdink.openai.parser.AiParsedWorkoutDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAiChatService {
    private final WebClient openAiWebClient;   // OpenAiConfig에서 만든 Bean
    private final OpenAiConfig openAiConfig;   // chatModel 가져다 쓰기 위함
    private final ObjectMapper objectMapper;   // JSON → DTO 역직렬화

    public AiParsedWorkoutDto parsedWorkoutDtoDto(String memoText, String ragHintText) {
        try {
            String prompt = buildPrompt(memoText, ragHintText);

            OpenAiChatRequest requestBody = buildRequestBody(prompt);

            OpenAiChatResponse response = openAiWebClient.post()
                    .uri("/chat/completions")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(OpenAiChatResponse.class)
                    .block();

            if (response == null
                    || response.choices() == null
                    || response.choices().isEmpty()
                    || response.choices().get(0).message() == null) {

                log.error("OpenAI 응답이 비어있음: {}", response);
                throw new CustomException(Error.NOT_FOUND_OPENAI, Error.NOT_FOUND_OPENAI.getMessage());
            }

            String json = response.choices().get(0).message().content();

            // LLM이 반환한 content(JSON 문자열)를 AiParsedWorkoutDto로 변환
            return objectMapper.readValue(json, AiParsedWorkoutDto.class);

        } catch (CustomException e) {
            // 이미 래핑된 예외는 그대로 던짐
            throw e;
        } catch (Exception e) {
            log.error("OpenAI AI 메모 파싱 실패", e);
            throw new CustomException(Error.FAILED_PARSE_OPENAI, Error.FAILED_PARSE_OPENAI.getMessage());
        }
    }

    /**
     * LLM에게 전달할 프롬프트 텍스트 구성
     */
    private String buildPrompt(String memoText, String ragHintText) {
        return """
                너는 헬스 운동 기록을 구조화된 JSON으로 변환하는 어시스턴트야.
                사용자가 자유 형식으로 적은 운동 기록을 아래 JSON 스키마에 맞게 변환해야 한다.
                
                JSON 스키마 (예시 형태):
                {
                  "exercises": [
                    {
                      "exerciseId": 1,
                      "exerciseName": "운동 이름",
                      "sets": [
                        { "setNumber": 1, "weight": 70, "reps": 8 }
                      ]
                    }
                  ]
                }
                
                반드시 아래 규칙을 지켜라:
                
                1. **반드시 순수 JSON만 반환해라.**
                   - 응답 전체가 '{' 로 시작해서 '}' 로 끝나는 하나의 JSON 객체여야 한다.
                   - 맨 앞이나 맨 뒤에 ``` 같은 문자를 절대 넣지 마라.
                   - ```json, ``` 와 같은 마크다운 코드 블록을 절대 사용하지 마라.
                   - "json:" 같은 설명 텍스트도 절대 붙이지 마라.
                
                2. **JSON 이외의 설명, 말투, 주석, 자연어 문장을 절대 쓰지 마라.**
                   - "다음은 JSON입니다" 같은 문장도 금지다.
                   - JSON 바깥에 어떤 글자도 추가하지 마라.
                
                3. 필드 규칙:
                   - exercises: 배열
                   - exerciseId: 정수(Number) 또는 null
                   - exerciseName: 문자열
                   - sets: 배열
                   - setNumber, weight, reps: 모두 정수(Number) 타입
                   - setNumber는 1부터 시작해서 세트 순서대로 1씩 증가시켜라.
                   - 후보 리스트에 있는 운동만 선택하고, 없다면 exerciseId와 exerciseName을 null로 둬라.
                   - 후보 리스트가 비어있지 않다면, 이름 또는 aliases가 입력에 포함되는 후보를 반드시 선택해라.
                   - 입력이 후보 이름/aliases의 일부이거나, 후보가 입력의 일부인 경우도 매칭으로 판단해라.
                   - 이름/aliases 비교는 공백, 하이픈, 대소문자를 무시한다.
                
                4. 사용자가 여러 줄로 운동을 적었다면, 각 줄을 하나의 exercise로 판단해라.
                예시:
                랫 풀다운 70 80 80 80 / 8-12reps
                데드리프트 120x12 140x3 180x3 200x1
                
                5. JSON 문법을 반드시 지켜라.
                   - 문자열은 항상 큰따옴표(")로 감싸라.
                   - 마지막 요소 뒤에 쉼표(,)를 붙이지 마라.
                   - null, true, false를 문자열이 아닌 리터럴로 쓸 땐 소문자로 써라.
                
                이제 아래 사용자의 운동 기록을 위 JSON 스키마에 맞게 변환해라.
                응답은 순수 JSON 하나만 포함해야 한다.
                
                후보 운동 리스트(형식: "id:1 name:운동명 part:부위"):
                """ + (ragHintText == null ? "(없음)" : ragHintText) + """
                
                사용자의 운동 기록:
                """ + memoText;
    }


    /**
     * OpenAI Chat Completions 요청 바디 구성
     */
    private OpenAiChatRequest buildRequestBody(String prompt) {

        OpenAiMessage systemMessage = new OpenAiMessage(
                "system",
                """
                        너는 헬스 운동 기록을 JSON으로 변환하는 파서다.
                        네 출력은 항상 **순수 JSON 하나**여야 한다.
                        
                        절대 하지 말아야 할 것:
                        - ```json, ``` 와 같은 마크다운 코드 블록 사용
                        - JSON 앞이나 뒤에 "다음은 JSON입니다" 같은 설명 추가
                        - JSON 바깥에 공백, 줄바꿈, 텍스트, 주석을 추가
                        
                        반드시 해야 할 것:
                        - 응답 전체가 '{' 로 시작해서 '}' 로 끝나는 유효한 JSON 객체여야 한다.
                        - 클라이언트는 네 응답을 그대로 파싱해서 ObjectMapper.readValue(...)에 넣을 것이다.
                        - 그러므로 유효하지 않은 JSON을 반환하면 안 된다.
                        - 후보 리스트에 없는 운동은 선택하지 말고 exerciseId와 exerciseName을 null로 둬라.
                        - 후보가 존재하면 가장 유사한 운동을 반드시 선택해라.
                        """
        );


        OpenAiMessage userMessage = new OpenAiMessage(
                "user",
                prompt
        );

        return new OpenAiChatRequest(
                openAiConfig.getChatModel(),
                List.of(systemMessage, userMessage)
        );
    }
}
