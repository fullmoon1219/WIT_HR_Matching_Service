package org.wit.hrmatching.controller.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wit.hrmatching.dto.applicant.ApplicantProfileDTO;
import org.wit.hrmatching.dto.applicant.ApplicantProfileUpdateRequestDTO;
import org.wit.hrmatching.service.applicant.ApplicantProfileService;
import org.wit.hrmatching.vo.user.CustomUserDetails;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ApplicantProfileRestController {

	private final ApplicantProfileService applicantProfileService;

	@GetMapping
	public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {

		long userId = userDetails.getId();

		ApplicantProfileDTO profile = applicantProfileService.getUserProfile(userId);

		return ResponseEntity.ok(profile);
	}

	@PutMapping
	public ResponseEntity<?> updateUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
											   @RequestBody ApplicantProfileUpdateRequestDTO applicantProfileUpdateRequestDTO) {

		try {
			long userId = userDetails.getId();
			applicantProfileService.updateUserProfile(userId, applicantProfileUpdateRequestDTO);

			return ResponseEntity.ok().build();

		} catch (BadCredentialsException e) {
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED) // 401 Unauthorized
					.body("비밀번호가 일치하지 않습니다.");

		} catch (Exception e) {
			return ResponseEntity
					.internalServerError() // 500 Internal Server Error
					.body("프로필 수정 중 오류가 발생했습니다.");
		}
	}

	@PostMapping("/image")
	public ResponseEntity<?> uploadProfileImage(@RequestParam("file") MultipartFile file,
												@AuthenticationPrincipal CustomUserDetails userDetails) {

		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("업로드할 파일이 없습니다.");
		}

		try {
			// Service에서 파일 저장
			String savedFilePath = applicantProfileService.updateProfileImage(userDetails.getId(), file);

			Map<String, String> response = new HashMap<>();
			response.put("filePath", savedFilePath);
			return ResponseEntity.ok(response);

		} catch (RuntimeException e) {
			// 파일 저장 중 에러 발생 시
			System.out.println("파일 저장 실패: " + e.getMessage());
			return ResponseEntity.internalServerError().body("파일 저장 중 오류가 발생했습니다.");
		}
	}
}
