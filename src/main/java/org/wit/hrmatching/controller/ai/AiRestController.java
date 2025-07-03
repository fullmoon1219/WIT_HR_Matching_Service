package org.wit.hrmatching.controller.ai;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.config.ai.JsonConverter;
import org.wit.hrmatching.config.ai.OpenAiResponseParser;
import org.wit.hrmatching.service.admin.AdminResumeService;
import org.wit.hrmatching.service.ai.AiService;
import org.wit.hrmatching.vo.ResumeVO;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiRestController {

    private final AiService aiService;
    private final AdminResumeService adminResumeService;
    private final JsonConverter jsonConverter;
    private final OpenAiResponseParser openAiResponseParser;

    @GetMapping("/evaluate-resume")
    public ResponseEntity<JsonNode> evaluateResume(@RequestParam Long id) {
        Pageable pageable = PageRequest.of(0, 1);
        Page<ResumeVO> page = adminResumeService.getPagedResumes(pageable, id, null, null, false, null);

        if (page.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(openAiResponseParser.parse("{\"error\": \"해당 이력서를 찾을 수 없습니다.\"}"));
        }

        ResumeVO resume = page.getContent().get(0);

        JsonNode resumeJson = jsonConverter.wrapWithContentArray(resume);

        String result = aiService.scoreResumeFromJson(resumeJson);

        JsonNode parsedResponse = openAiResponseParser.parse(result);

        return ResponseEntity.ok(parsedResponse);
    }


}
