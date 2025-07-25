package org.wit.hrmatching.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.wit.hrmatching.vo.user.CustomUserDetails;
import org.wit.hrmatching.service.auth.AuthService;
import org.wit.hrmatching.vo.user.UserVO;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final AuthService authService;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        UserVO user = null;
        if (userDetails != null) {
            user = authService.getUserWithProfile(userDetails.getUser().getId());
        }
        model.addAttribute("userDetails", user);

        return "index";
    }
}

