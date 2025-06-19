package org.wit.hrmatching.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.wit.hrmatching.config.auth.CustomUserDetails;
import org.wit.hrmatching.service.auth.UserService;
import org.wit.hrmatching.vo.UserVO;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        UserVO user = null;
        if (userDetails != null) {
            user = userService.getUserWithProfile(userDetails.getUser().getId());
        }
        model.addAttribute("userDetails", user);

        return "index";
    }
}

