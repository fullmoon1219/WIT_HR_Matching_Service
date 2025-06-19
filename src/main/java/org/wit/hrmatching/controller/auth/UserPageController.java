package org.wit.hrmatching.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.dto.login.UserRegisterDTO;
import org.wit.hrmatching.service.auth.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserPageController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String oauth2error,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "이메일 또는 비밀번호가 올바르지 않습니다.");
        }
        if (oauth2error != null) {
            model.addAttribute("errorMessage", "소셜 로그인에 실패했습니다. 다시 시도해주세요.");
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
}
