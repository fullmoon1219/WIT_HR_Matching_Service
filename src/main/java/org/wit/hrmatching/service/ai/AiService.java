package org.wit.hrmatching.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.wit.hrmatching.enums.AiRole;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "openai.api-key")
public class AiService {

    @Value("${openai.api-key}") private String apiKey;
    @Value("${openai.api-url}") private String apiUrl;
    @Value("${openai.model}") private String model;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    private String callGptWithPrompt( String systemMessage, String userPrompt) {
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", systemMessage),
                        Map.of("role", "user", "content", userPrompt)
                )
        );

        return webClient.post()
                .uri(apiUrl)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 동기 처리 (테스트용)
    }

    private String getText(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null && !field.isNull() ? field.asText() : "(없음)";
    }

    public String scoreResumeFromJson(JsonNode resumeJson) {
        JsonNode data = resumeJson.get("content").get(0);  // 이력서 1건

        // 프롬프트 생성
        String prompt = String.format("""
            너는 인사담당자야.
            아래 이력서를 평가해서 점수를 매기고 보완점을 알려줘.

            1. 총점 (100점 만점)
            2. 강점 요약
            3. 보완이 필요한 항목과 이유 (3개 이상)

            ---
            이력서 정보:
            - 학력: %s
            - 경력: %s
            - 기술스택: %s
            - 희망 지역: %s
            - 희망 연봉: %s만원
            - 지원 분야: %s
            - 핵심 역량: %s
            - 지원 동기: %s
            """,
                getText(data, "education"),
                getText(data, "experience"),
                getText(data, "skills"),
                getText(data, "preferredLocation"),
                getText(data, "salaryExpectation"),
                getText(data, "desiredPosition"),
                getText(data, "coreCompetency"),
                getText(data, "motivation")
        );

        return callGptWithPrompt(AiRole.HR_REVIEWER.message(), prompt);
    }
}
