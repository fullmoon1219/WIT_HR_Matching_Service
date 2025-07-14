package org.wit.hrmatching.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.vo.user.CustomUserDetails;
import org.wit.hrmatching.service.auth.oAuth2.CustomOAuth2DisconnectService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserAuthController {

    private final CustomOAuth2DisconnectService customOAuth2DisconnectService;

    // 로그인 상태 확인
    private boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null &&
                auth.isAuthenticated() &&
                !(auth instanceof AnonymousAuthenticationToken);
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String oauth2error,
                            HttpServletRequest request,
                            Model model) {

        if (isLoggedIn()) {
            System.out.println("현재 로그인중입니다.");
            return "redirect:/";
        }

        String referer = request.getHeader("Referer");
        if (referer != null) {
            request.getSession().setAttribute("prevPage", referer);
        }

        String errorMessage = (String) request.getSession().getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            request.getSession().removeAttribute("errorMessage");
        }

        return "account/login";
    }

    // 회원가입 폼
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        if (isLoggedIn()) {
            return "redirect:/";
        }

        model.addAttribute("userRegisterDTO", new org.wit.hrmatching.dto.login.UserRegisterDTO());
        return "account/register";
    }

    // 회원가입 성공 화면
    @GetMapping("/register-success")
    public String registerSuccess() {
        if (isLoggedIn()) {
            return "redirect:/";
        }
        return "account/register-success";
    }

    // 로그아웃 요청
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return "redirect:/";
    }

    // 로그아웃 완료 후 화면
    @GetMapping("/logout-success")
    public String logoutSuccessPage() {
        return "index";
    }

    // 이메일 인증 결과 화면만 표시
    @GetMapping("/verify")
    public String verifyPage(@RequestParam("token") String token) {

        return "account/mail-verity";
    }

    // 회원탈퇴 폼
    @GetMapping("/delete")
    public String showDeletePage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        boolean isSocialUser = userDetails.getUser().isSocialUser();

        model.addAttribute("isSocialUser", isSocialUser);
        return "account/delete";
    }

    // 회원탈퇴 성공 페이지
    @GetMapping("/delete-success")
    public String deleteSuccessPage(HttpSession session) {
        Boolean deleted = (Boolean) session.getAttribute("deleted");

        if (deleted != null && deleted) {
            session.removeAttribute("deleted");
            return "account/delete-success";
        }

        return "redirect:/"; // 플래그가 없으면 홈으로 리다이렉트
    }

}
