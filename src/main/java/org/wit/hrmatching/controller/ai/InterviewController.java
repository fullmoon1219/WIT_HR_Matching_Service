package org.wit.hrmatching.controller.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/interview")
public class InterviewController {

    @GetMapping
    public String interviewPage() {
        return "interview/ai-test";
    }

    @GetMapping("/answer/{resumeId}")
    public String interviewAnswerPage(@PathVariable String resumeId, Model model) {
        return "interview/ai-interview";
    }

    @GetMapping("/evaluation")
    public String interviewEvaluationPage() {
        return "interview/ai-feedback";
    }
}
