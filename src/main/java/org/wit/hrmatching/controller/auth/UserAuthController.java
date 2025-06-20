package org.wit.hrmatching.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wit.hrmatching.config.auth.CustomUserDetails;
import org.wit.hrmatching.dto.login.UserRegisterDTO;
import org.wit.hrmatching.service.auth.UserService;
import org.wit.hrmatching.vo.UserVO;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserAuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String oauth2error,
                            HttpServletRequest request,
                            Model model) {

        String errorMessage = (String) request.getSession().getAttribute("errorMessage");

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            request.getSession().removeAttribute("errorMessage"); // 1회성 표시 후 제거
        }

        return "login/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userRegisterDTO", new UserRegisterDTO());
        return "login/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute UserRegisterDTO userRegisterDTO,
                               BindingResult bindingResult,
                               Model model) {

        // 비밀번호 확인 검사
        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "비밀번호가 일치하지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            return "login/register";
        }

        try {
            // 회원가입 처리
            userService.registerUser(userRegisterDTO);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "login/register";
        }

        return "redirect:/users/register-success";
    }

    @GetMapping("/register-success")
    public String registerSuccess() {
        return "login/register-success";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();

        return "redirect:/";
    }

    @GetMapping("/logout-success")
    public String logoutSuccessPage() {
        return "index";
    }

    @GetMapping("/verify")
    public String verifyUserEmail(@RequestParam("token") String token, Model model) {
        UserVO user = userService.findByVerificationToken(token);

        if (user == null) {
            model.addAttribute("message", "유효하지 않은 인증 토큰입니다.");
            return "login/mail-verity";
        }

        // 만료 시간 체크
        if (user.getTokenExpiration() != null && user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            model.addAttribute("message", "인증 토큰이 만료되었습니다. 다시 시도해주세요.");
            return "login/mail-verity";
        }

        // 인증 완료 처리
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setTokenExpiration(null);
        userService.updateUser(user);

        model.addAttribute("message", "이메일 인증이 성공적으로 완료되었습니다!");
        return "login/mail-verity";
    }

    @PostMapping("/delete")
    public String deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        Long userId = userDetails.getUser().getId();
        userService.deleteUserAccount(userId);

        // 로그아웃 처리
        SecurityContextHolder.clearContext();

        redirectAttributes.addFlashAttribute("message", "회원 탈퇴가 완료되었습니다.");
        return "redirect:/login?logout";
    }

}
