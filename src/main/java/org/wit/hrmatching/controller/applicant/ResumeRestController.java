package org.wit.hrmatching.controller.applicant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wit.hrmatching.service.applicant.ResumeService;
import org.wit.hrmatching.vo.CustomUserDetails;
import org.wit.hrmatching.vo.ResumeVO;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resume")
public class ResumeRestController {

	private final ResumeService resumeService;

	@PostMapping
	public ResponseEntity<?> registerResume(@AuthenticationPrincipal CustomUserDetails userDetails,
											@Valid @RequestBody ResumeVO resumeVO,
											BindingResult bindingResult) {

		Long userId = userDetails.getId();
		resumeVO.setUserId(userId);

		// 유효성 오류 처리
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(Map.of("success", false,
					"errors", bindingResult.getAllErrors()));
		}

		boolean result = resumeService.registerResume(resumeVO);

		if (result) {
			return ResponseEntity.ok(Map.of(
					"success", true,
					"id", resumeVO.getId()
			));
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
					"success", false,
					"message", "DB 저장 실패"
			));
		}
	}
}
