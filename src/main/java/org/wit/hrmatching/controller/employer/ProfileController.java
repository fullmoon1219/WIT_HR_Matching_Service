package org.wit.hrmatching.controller.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.wit.hrmatching.service.UserService;
import org.wit.hrmatching.service.employer.ProfileService;
import org.wit.hrmatching.vo.CustomUserDetails;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.wit.hrmatching.vo.EmployerRecentApplicantVO;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employer/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 기업정보 보기
    @GetMapping("/view")
    public ModelAndView viewProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        EmployerProfilesVO profile = profileService.getEmployerProfile(userId);
        List<EmployerRecentApplicantVO> vo = profileService.selectEmployerRecentApplicantList(userId);

        ModelAndView modelAndView = new ModelAndView("employer/profile/view");
        modelAndView.addObject("profile", profile);
        modelAndView.addObject("recentApplicantList", vo);

        return modelAndView;
    }

    // 기업정보 수정 (employer_profiles + users.name)
    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<String> updateProfile(@RequestBody EmployerProfilesVO profile,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        String encodedPassword = userDetails.getUser().getPassword();
        String rawPassword = profile.getPassword();

        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("비밀번호가 일치하지 않습니다.");
        }
        profile.setUserId(userId);

        // ✅ 여기에서 updateFullProfile 사용
        profileService.updateFullProfile(profile);

        return ResponseEntity.ok("기업 정보가 성공적으로 수정되었습니다.");
    }

    // 비밀번호 변경시, 비밀번호 검증
    @PostMapping("/verify_password")
    @ResponseBody
    public ResponseEntity<String> verifyPassword(@RequestBody Map<String, String> body,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        String encodedPassword = userDetails.getUser().getPassword();
        String rawPassword = body.get("password");

        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("비밀번호가 일치하지 않습니다.");
        }

        return ResponseEntity.ok("ok");
    }

    // 비밀번호 변경
    @PostMapping("/update_password")
    @ResponseBody
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> body,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        String rawPassword = body.get("newPassword");

        String encodedPassword = passwordEncoder.encode(rawPassword);
        int flag = userService.updatePassword(userId, encodedPassword); // 실제 업데이트 수행

        if (flag>0) return ResponseEntity.ok("비밀번호 변경 완료");
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 실패");
    }
}
