package org.wit.hrmatching.config.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenAiResponseParser {

    private final ObjectMapper objectMapper;

    public JsonNode parse(String rawJson) {
        try {
            return objectMapper.readTree(rawJson);
        } catch (Exception e) {
            // 파싱 실패 시 에러 JSON 반환
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("error", "OpenAI 응답 파싱 실패");
            return errorNode;
        }
    }
}
