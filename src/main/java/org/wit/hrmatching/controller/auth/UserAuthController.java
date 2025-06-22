package org.wit.hrmatching.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wit.hrmatching.vo.CustomUserDetails;
import org.wit.hrmatching.dto.login.UserRegisterDTO;
import org.wit.hrmatching.service.auth.AuthService;
import org.wit.hrmatching.service.auth.oAuth2.CustomOAuth2DisconnectService;
import org.wit.hrmatching.vo.UserVO;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserAuthController {

    private final AuthService authService;
    private final CustomOAuth2DisconnectService customOAuth2DisconnectService;
    private final PasswordEncoder passwordEncoder;

    // 로그인 상태인지 구분
    private boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
    }


    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String oauth2error,
                            HttpServletRequest request,
                            Model model) {

        if (isLoggedIn()) {
            return "redirect:/";
        }

        String referer = request.getHeader("Referer");
        if (referer != null) {
            request.getSession().setAttribute("prevPage", referer);
        }

        String errorMessage = (String) request.getSession().getAttribute("errorMessage");

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            request.getSession().removeAttribute("errorMessage"); // 1회성 표시 후 제거
        }

        return "account/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        if (isLoggedIn()) {
            return "redirect:/";
        }

        model.addAttribute("userRegisterDTO", new UserRegisterDTO());
        return "account/register";
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
            return "account/register";
        }

        try {
            // 회원가입 처리
            authService.registerUser(userRegisterDTO);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "account/register";
        }

        return "redirect:/users/register-success";
    }

    @GetMapping("/register-success")
    public String registerSuccess() {
        if (isLoggedIn()) {
            return "redirect:/";
        }

        return "account/register-success";
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
        UserVO user = authService.findByVerificationToken(token);

        if (user == null) {
            model.addAttribute("message", "유효하지 않은 인증 토큰입니다.");
            return "account/mail-verity";
        }

        // 만료 시간 체크
        if (user.getTokenExpiration() != null && user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            model.addAttribute("message", "인증 토큰이 만료되었습니다. 다시 시도해주세요.");
            return "account/mail-verity";
        }

        // 인증 완료 처리
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setTokenExpiration(null);
        authService.updateUser(user);

        model.addAttribute("message", "이메일 인증이 성공적으로 완료되었습니다!");
        return "account/mail-verity";
    }

    @GetMapping("/delete")
    public String showDeletePage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        UserVO user = userDetails.getUser();

        boolean isSocialUser = user.isSocialUser();

        model.addAttribute("isSocialUser", isSocialUser);

        return "account/delete";
    }

    @PostMapping("/delete")
    public String deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @RequestParam(value = "password", required = false) String password,
                                @RequestParam(value = "confirmDelete", required = false) String confirmDelete,
                                RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");

            return "redirect:/users/login";
        }

        UserVO user = authService.findByEmail(userDetails.getEmail());

        boolean isSocialUser = user.isSocialUser();

        if (!isSocialUser) {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                redirectAttributes.addFlashAttribute("message", "비밀번호가 일치하지 않습니다.");

                return "redirect:/users/delete";
            }
        } else {
            if (confirmDelete == null) {
                redirectAttributes.addFlashAttribute("message", "탈퇴에 동의해주세요.");
                return "redirect:/users/delete";
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken token) {
            String registrationId = token.getAuthorizedClientRegistrationId();
            customOAuth2DisconnectService.disconnectFromSocial(token, registrationId);

            System.out.println("소셜 로그인 연결 해제");
        }

        authService.deleteUserAccount(user.getId());
        SecurityContextHolder.clearContext(); // 로그아웃 처리

        redirectAttributes.addFlashAttribute("message", "회원 탈퇴가 완료되었습니다.");
        return "account/delete-success";
    }

}
