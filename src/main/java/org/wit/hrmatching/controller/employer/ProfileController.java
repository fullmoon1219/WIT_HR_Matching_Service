package org.wit.hrmatching.controller.employer;

import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employer/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;
    private final ServletContext servletContext;

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

    @PostMapping("/image-upload")
    @ResponseBody
    public Map<String, Object> uploadProfileImage(@RequestParam("profileImage") MultipartFile file,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, Object> result = new HashMap<>();
        Long userId = userDetails.getUser().getId();

        try {
            // ✅ static/uploads 경로로 저장
            String resourcePath = new ClassPathResource("static/uploads").getFile().getAbsolutePath();
            String originalFilename = file.getOriginalFilename();
            String storedName = UUID.randomUUID() + "_" + originalFilename;

            // 저장 경로 생성
            File directory = new File(resourcePath);
            if (!directory.exists()) directory.mkdirs();

            // 저장
            File savedFile = new File(directory, storedName);
            file.transferTo(savedFile);

            // DB 저장
            profileService.updateProfileImage(userId, storedName);

            // 응답
            result.put("success", true);
            result.put("imageUrl", "/uploads/" + storedName); // 정적 접근 경로
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            e.printStackTrace(); // 로그 출력
        }

        return result;
    }

}
