package org.wit.hrmatching.config.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonConverter {

    private final ObjectMapper objectMapper;

    public <T> JsonNode wrapWithContentArray(T vo) {
        ObjectNode wrapper = objectMapper.createObjectNode();
        ArrayNode contentArray = objectMapper.createArrayNode();
        JsonNode node = objectMapper.valueToTree(vo);
        contentArray.add(node);
        wrapper.set("content", contentArray);
        return wrapper;
    }
}
