package org.wit.hrmatching.config.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenAiResponseParser {

    public static JsonNode parseJsonResponse(String gptRaw) {
        String raw = gptRaw.trim();
        if (raw.startsWith("```json")) {
            raw = raw.replaceFirst("```json", "").trim();
        }
        if (raw.endsWith("```")) {
            raw = raw.substring(0, raw.lastIndexOf("```")).trim();
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(raw);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("GPT 응답을 JSON으로 파싱할 수 없습니다", e);
        }
    }

    public static String extractContentFromGptChoices(String gptJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(gptJson);
            return root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText()
                    .trim();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("GPT 응답에서 요약을 추출하지 못했습니다", e);
        }
    }
}
