package org.wit.hrmatching.controller.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.dto.applicant.ProfileDTO;
import org.wit.hrmatching.dto.applicant.ProfileUpdateRequestDTO;
import org.wit.hrmatching.service.applicant.ProfileService;
import org.wit.hrmatching.vo.user.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileRestController {

	private final ProfileService profileService;

	@GetMapping
	public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {

		long userId = userDetails.getId();

		ProfileDTO profile = profileService.getUserProfile(userId);

		return ResponseEntity.ok(profile);
	}

	@PutMapping
	public ResponseEntity<?> updateUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
											   @RequestBody ProfileUpdateRequestDTO profileUpdateRequestDTO) {

		try {
			long userId = userDetails.getId();
			profileService.updateUserProfile(userId, profileUpdateRequestDTO);

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
}
