package org.wit.hrmatching.controller.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/support")
public class SupportController {

    @GetMapping("/inquiry")
    public String showInquiry() {
        return "support/inquiry";
    }

    @PostMapping("/report")
    public String showReport(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long targetId,
            @RequestParam(required = false) Long reportedUserId,
            Model model) {
        model.addAttribute("type", type);
        model.addAttribute("targetId", targetId);
        model.addAttribute("reportedUserId", reportedUserId);
        return "support/report";
    }
}
