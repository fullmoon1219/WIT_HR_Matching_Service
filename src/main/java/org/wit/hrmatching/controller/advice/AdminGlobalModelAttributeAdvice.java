package org.wit.hrmatching.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.wit.hrmatching.controller.admin.AdminController;
import org.wit.hrmatching.vo.CustomUserDetails;
import org.wit.hrmatching.vo.UserVO;

//@ControllerAdvice(basePackages = "org.wit.hrmatching.controller.admin")
@ControllerAdvice(assignableTypes = {AdminController.class})
@RequiredArgsConstructor
public class AdminGlobalModelAttributeAdvice {

    @ModelAttribute("admin")
    public UserVO addAdminUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userDetails != null ? userDetails.getUser() : null;
    }

    @ModelAttribute("ip")
    public String addIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return (ip == null || ip.isEmpty()) ? request.getRemoteAddr() : ip;
    }
}
