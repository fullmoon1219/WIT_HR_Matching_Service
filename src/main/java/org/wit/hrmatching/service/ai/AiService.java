package org.wit.hrmatching.service.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.wit.hrmatching.config.ai.OpenAiResponseParser;
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

    public JsonNode scoreResumeFromJson(JsonNode resumeJson) {
        JsonNode data = resumeJson.get("content").get(0);

        String prompt = String.format("""
                너는 10년 이상 경력의 인사담당자야.
                다음 이력서를 평가해서 아래 항목을 작성해줘.
                
                [요청 형식]
                아래와 같은 JSON 형식으로만 응답해. 설명 없이 JSON만 반환해:
                
                {
                  "score": 0,                       // 숫자만
                  "strengths": "string",            // 강점이 없으면 '강점 없음'
                  "weaknesses": ["...", "..."],    // 구체적인 보완점 3개 이상
                  "comment": "string"              // 전체적 마무리 코멘트 (형식적 표현 금지)
                }
                
                [주의]
                - 반드시 JSON만 반환해 (앞뒤 설명 없이)
                - 키 이름과 형식은 그대로 지켜야 함
                
                [요청 사항]
                1. 총점 (100점 만점, 반드시 아래 기준을 지켜서 숫자로만)
                2. 강점 요약 (실질적인 강점이 있을 경우에만 작성)
                3. 보완이 필요한 항목과 그 이유 (3개 이상)
                4. 내용은 현실적으로 냉정하게 작성해줘. 형식적 표현은 지양하고 구체적으로 지적해.
                
                [점수 기준]
                - 90점 이상: 탁월함
                - 80~89점: 우수
                - 70~79점: 평균 이상
                - 60~69점: 평균
                - 50~59점: 부족
                - 40~49점: 매우 미흡
                - 39점 이하: 작성 수준 미달
                
                [주의]
                - 강점이 뚜렷하지 않으면 "강점 없음"이라고 작성해.
                - 과장 표현은 쓰지 말고 실무 기준으로만 구체적이고 냉정하게 평가해.
                - 경력과 기술이 부족하면 점수에 반영하고 그 이유를 설명해.
                
                [이력서 정보]
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

        String result = callGptWithPrompt(AiRole.HR_REVIEWER.message(), prompt);
        String content = OpenAiResponseParser.extractContentFromGptChoices(result);

        return OpenAiResponseParser.parseJsonResponse(content);
    }

    public String summarizeResumeForRecruiter(JsonNode resumeJson) {
        JsonNode data = resumeJson.get("content").get(0);

        String prompt = String.format("""
                        너는 10년 경력의 채용 담당자야. 아래 이력서를 실무 기준으로 냉정하게 평가해서, 이 사람이 어떤 인재인지 간결하게 요약해줘.
                        
                        [요약 작성 규칙]
                        - 2~3문장 이내로 요약
                        - 실무에서 쓸 수 있는 강점이 있다면 강조
                        - 강점이 부족하거나 없음이 명확하면 '강점 없음'이라고 분명하게 작성
                        - 과장하거나 좋게 포장하지 마. 진짜 실무에서 채용 여부 판단에 도움이 되는 정보만 보여줘
                        - 추상적인 미사여구는 금지. 구체적으로 표현할 것
                        
                        [이력서 정보]
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

        String result = callGptWithPrompt(AiRole.HR_REVIEWER.message(), prompt);

        return OpenAiResponseParser.extractContentFromGptChoices(result);
    }


    public List<String> generateInterviewQuestions(JsonNode resumeJson) {
        JsonNode data = resumeJson.get("content").get(0);

        String prompt = String.format("""
        너는 10년 경력의 실무 면접관이야.
        아래 이력서를 참고해서 실무 기반의 백엔드 면접 질문 5가지를 생성해줘.

        [요청 형식]
        - 반드시 JSON 배열로만 응답해. 예: ["질문1", "질문2", ...]
        - 설명 없이 배열만 반환

        [이력서 정보]
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

        String result = callGptWithPrompt(AiRole.INTERVIEWER.message(), prompt);
        String content = OpenAiResponseParser.extractContentFromGptChoices(result);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(content, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("면접 질문 JSON 파싱 실패", e);
        }
    }

    public String evaluateInterviewAnswer(String question, String answer) {
        String prompt = String.format("""
        너는 10년 경력의 면접관이야.
        아래는 지원자의 답변이야. 냉정하게 평가해주고, 개선할 점이 있다면 제시해줘.

        [질문]
        %s

        [답변]
        %s

        [요청 형식]
        - 2~3문장 내외의 피드백만 제공
        - 반드시 실무 기준으로 평가
        - 형식적 표현 없이 솔직하게 평가
        """, question, answer);

        String result = callGptWithPrompt(AiRole.INTERVIEWER.message(), prompt);
        return OpenAiResponseParser.extractContentFromGptChoices(result);
    }
}
