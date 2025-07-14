package org.wit.hrmatching.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.dto.login.UserRegisterDTO;
import org.wit.hrmatching.service.UserService;
import org.wit.hrmatching.service.auth.AuthService;
import org.wit.hrmatching.service.auth.oAuth2.CustomOAuth2DisconnectService;
import org.wit.hrmatching.vo.user.CustomUserDetails;
import org.wit.hrmatching.vo.user.UserVO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserAuthRestController {

    private final AuthService authService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CustomOAuth2DisconnectService customOAuth2DisconnectService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        try {
            authService.registerUser(dto);
            return ResponseEntity.ok("회원가입 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @RequestBody Map<String, String> payload,
                                           HttpServletRequest request) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        UserVO user = authService.findByEmail(userDetails.getEmail());
        boolean isSocialUser = user.isSocialUser();

        String password = payload.get("password");
        String confirmDelete = payload.get("confirmDelete");

        System.out.println("password: " + password);

        if (!isSocialUser && (password == null || !passwordEncoder.matches(password, user.getPassword()))) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        if (isSocialUser && confirmDelete == null) {
            return ResponseEntity.badRequest().body("탈퇴에 동의해주세요.");
        }

        authService.deleteUserAccount(user.getId());
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        request.getSession().setAttribute("deleted", true);

        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }


    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        UserVO user = authService.findByVerificationToken(token);
        Map<String, String> result = new HashMap<>();

        if (user == null) {
            result.put("message", "유효하지 않은 인증 토큰입니다.");
            return ResponseEntity.badRequest().body(result);
        }

        if (user.getTokenExpiration() != null && user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            result.put("message", "인증 토큰이 만료되었습니다.");
            return ResponseEntity.badRequest().body(result);
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setTokenExpiration(null);
        authService.updateUser(user);

        result.put("message", "이메일 인증이 완료되었습니다.");
        return ResponseEntity.ok(result);
    }

    @GetMapping("/email-exists")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        System.out.println("이메일 중복 확인");
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return ResponseEntity.badRequest().body(Map.of("valid", false));
        }

        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
