package org.wit.hrmatching.controller.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/support")
public class SupportController {

    @GetMapping("/inquiry")
    public String showInquiry() {
        return "support/inquiry";
    }
}
