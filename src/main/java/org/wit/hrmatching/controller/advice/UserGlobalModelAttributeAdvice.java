package org.wit.hrmatching.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.wit.hrmatching.controller.ai.InterviewController;
import org.wit.hrmatching.controller.community.CommunityController;
import org.wit.hrmatching.controller.community.CommunityRestController;
import org.wit.hrmatching.controller.employer.*;
import org.wit.hrmatching.controller.users.SupportController;
import org.wit.hrmatching.vo.user.CustomUserDetails;
import org.wit.hrmatching.vo.user.UserVO;

//@ControllerAdvice(basePackages = "org.wit.hrmatching.controller.admin")
@ControllerAdvice(assignableTypes = {CommunityController.class, CommunityRestController.class, SupportController.class, JobPostController.class,
        EmpApplicationController.class, EmployerProfileController.class, EmployerProfileController.class, PublicResumesController.class,
        InterviewController.class},

        basePackages = {"org.wit.hrmatching.controller.applicant", "org.wit.hrmatching.controller.common"})
@RequiredArgsConstructor
public class UserGlobalModelAttributeAdvice {

    @ModelAttribute("userDetails")
    public UserVO addAdminUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userDetails != null ? userDetails.getUser() : null;
    }

    @ModelAttribute("ip")
    public String addIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return (ip == null || ip.isEmpty()) ? request.getRemoteAddr() : ip;
    }
}
