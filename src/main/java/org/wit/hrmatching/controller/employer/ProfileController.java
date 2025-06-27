package org.wit.hrmatching.controller.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wit.hrmatching.service.employer.ProfileService;
import org.wit.hrmatching.vo.CustomUserDetails;
import org.wit.hrmatching.vo.EmployerProfilesVO;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employer/profile")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/view")
    public ModelAndView viewProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUser().getId();
        EmployerProfilesVO profile = profileService.getEmployerProfile(userId);

        ModelAndView modelAndView = new ModelAndView("employer/profile/view"); // 뷰 이름 지정
        modelAndView.addObject("profile", profile); // model.addAttribute 와 같음
        return modelAndView;
    }

}

