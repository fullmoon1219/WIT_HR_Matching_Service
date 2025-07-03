package org.wit.hrmatching.controller.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.wit.hrmatching.service.employer.ProfileService;
import org.wit.hrmatching.vo.CustomUserDetails;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.wit.hrmatching.vo.EmployerRecentApplicantVO;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employer/profile")
public class ProfileController {

    private final ProfileService profileService;

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

//        profile.setUserId(userId);

        // ✅ employer_profiles 업데이트 + users.name 업데이트
//        boolean updated = profileService.updateFullProfile(profile);
//
//        if (updated) {
//            return ResponseEntity.ok("기업 정보가 성공적으로 수정되었습니다.");
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("기업 정보 수정에 실패했습니다.");
//        }
        profile.setUserId(userId);

        // ✅ 여기에서 updateFullProfile 사용
        profileService.updateFullProfile(profile);

        return ResponseEntity.ok("기업 정보가 성공적으로 수정되었습니다.");
    }
}
