package org.wit.hrmatching.controller.employer;

import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.wit.hrmatching.config.file.FileUploadProperties;
import org.wit.hrmatching.service.UserService;
import org.wit.hrmatching.service.employer.EmployerProfileService;
import org.wit.hrmatching.vo.user.CustomUserDetails;
import org.wit.hrmatching.vo.user.EmployerProfilesVO;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.wit.hrmatching.vo.application.EmployerRecentApplicantVO;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employer/profile")
public class EmployerProfileController {

    private final EmployerProfileService employerProfileService;
    private final UserService userService;
    private final ServletContext servletContext;
    private final FileUploadProperties fileUploadProperties;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 기업정보 보기
    @GetMapping("/view")
    public ModelAndView viewProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        EmployerProfilesVO profile = employerProfileService.getEmployerProfile(userId);
        List<EmployerRecentApplicantVO> vo = employerProfileService.selectEmployerRecentApplicantList(userId);

        String imageUrl = (profile.getProfileImage() != null)
                ? "/uploads/users/profile/" + profile.getProfileImage()
                : "/images/users/user_small_profile.png";
        ModelAndView modelAndView = new ModelAndView("employer/profile/view");
        modelAndView.addObject("profile", profile);
        modelAndView.addObject("recentApplicantList", vo);
        modelAndView.addObject("profileImageUrl", imageUrl);

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
        employerProfileService.updateFullProfile(profile);

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
            // ✅ yml에서 설정한 경로 사용
            String baseDir = fileUploadProperties.getUserProfile().replaceAll("[/\\\\]?$", "/");
            String originalFilename = file.getOriginalFilename();
            String storedName = UUID.randomUUID() + "_" + originalFilename;

            String uploadPath = baseDir + storedName;

            // ✅ 디렉토리 확인 및 생성
            File targetFile = new File(uploadPath);
            File parentDir = targetFile.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                throw new RuntimeException("업로드 디렉토리 생성 실패: " + parentDir.getAbsolutePath());
            }

            // ✅ 저장
            file.transferTo(targetFile);

            // ✅ DB 저장
            employerProfileService.updateProfileImage(userId, storedName);

            // ✅ 응답
            result.put("success", true);
            result.put("imageUrl", "/uploads/" + storedName); // 프론트에서 이 경로로 접근할 수 있도록 설정 필요
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
}
