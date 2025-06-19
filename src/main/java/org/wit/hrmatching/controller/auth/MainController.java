package org.wit.hrmatching.controller.auth;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.wit.hrmatching.config.auth.CustomUserDetails;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("userDetails", userDetails);
        return "index";
    }
}

