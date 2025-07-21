package org.wit.hrmatching.controller.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.service.ai.AiService;
import org.wit.hrmatching.service.applicant.ResumeService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@ConditionalOnBean(AiService.class)
public class AiRestController {

    private final AiService aiService;
    private final ResumeService resumeService;

    @PostMapping("/resumes/analyze")
    public ResponseEntity<JsonNode> analyzeResume(@RequestBody Map<String, Long> payload) {
        Long resumeId = payload.get("resumeId");
        JsonNode resumeJson = resumeService.getResumeJsonById(resumeId);
        JsonNode result = aiService.scoreResumeFromJson(resumeJson);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/resumes/summary")
    public ResponseEntity<JsonNode> summarizeForCompany(@RequestBody Map<String, Long> payload) {
        Long resumeId = payload.get("resumeId");
        JsonNode resumeJson = resumeService.getResumeJsonById(resumeId);
        String summaryText = aiService.summarizeResumeForRecruiter(resumeJson);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("summary", summaryText);

        return ResponseEntity.ok(root);
    }

    @PostMapping("/interview/questions")
    public ResponseEntity<List<String>> generateQuestionsFromResume(@RequestBody Map<String, Long> payload) {
        Long resumeId = payload.get("resumeId");
        JsonNode resumeJson = resumeService.getResumeJsonById(resumeId);
        List<String> questions = aiService.generateInterviewQuestions(resumeJson);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/interview/evaluate")
    public String evaluateAnswer(@RequestBody Map<String, String> payload) {
        String question = payload.get("question");
        String answer = payload.get("answer");
        return aiService.evaluateInterviewAnswer(question, answer);
    }
}
