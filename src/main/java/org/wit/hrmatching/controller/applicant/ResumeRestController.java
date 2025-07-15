package org.wit.hrmatching.controller.applicant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.exception.IncompleteProfileException;
import org.wit.hrmatching.exception.ResumeNotFoundException;
import org.wit.hrmatching.service.applicant.ResumeService;
import org.wit.hrmatching.vo.user.CustomUserDetails;
import org.wit.hrmatching.vo.resume.ResumeVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes")
public class ResumeRestController {

	private final ResumeService resumeService;

	@GetMapping
	public ResponseEntity<Map<String, Object>> getUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {

		Long userId = userDetails.getId();

		Map<String, Object> userProfile = resumeService.getUserProfile(userId);

		return ResponseEntity.ok(userProfile);
	}

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

		return result
				? ResponseEntity.ok(Map.of("success", true, "id", resumeVO.getId()))
				: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("success", false, "message", "DB 저장 실패"));
	}

	@PostMapping("/draft")
	public ResponseEntity<?> saveDraftResume(@AuthenticationPrincipal CustomUserDetails userDetails,
											 @RequestBody ResumeVO resumeVO) {
		resumeVO.setUserId(userDetails.getId());

		boolean result = resumeService.registerResume(resumeVO);

		return result
				? ResponseEntity.ok(Map.of("success", true))
				: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("success", false, "message", "DB 저장 실패"));
	}

	// 완성된 이력서 목록 조회
	@GetMapping("/completed")
	public List<ResumeVO> getCompletedResumeList(@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getId();
		return resumeService.getCompletedResumeList(userId);
	}

	// 임시 저장 이력서 목록 조회
	@GetMapping("/draft")
	public List<ResumeVO> getDraftResumeList(@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getId();
		return resumeService.getDraftResumeList(userId);
	}

	// 대표 이력서 해제
	@PutMapping("/{resumeId}/private")
	@PreAuthorize("@permission.isResumeOwner(#resumeId, authentication)")
	public ResponseEntity<?> setResumePrivate(@PathVariable Long resumeId,
											  @AuthenticationPrincipal CustomUserDetails userDetails) {

		boolean result = resumeService.setResumeAsPrivate(resumeId, userDetails.getId());

		return result ? ResponseEntity.ok().build()
				: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	// 대표 이력서 설정
	@PutMapping("/{resumeId}/public")
	@PreAuthorize("@permission.isResumeOwner(#resumeId, authentication)")
	public ResponseEntity<?> setResumePublic(@PathVariable Long resumeId,
											 @AuthenticationPrincipal CustomUserDetails userDetails) {

		try {
			resumeService.setResumeAsPublic(resumeId, userDetails.getId());
			return ResponseEntity.ok().build();

		} catch (IncompleteProfileException e) {
			// 프로필 미완성 예외
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
		}
	}

	// 이력서 상세보기 (수정 시 기존 이력서 데이터도 전달)
	@GetMapping("/{resumeId}")
	@PreAuthorize("@permission.isResumeOwner(#resumeId, authentication)")
	public ResponseEntity<?> getResume(@PathVariable Long resumeId) {

		ResumeVO resume = resumeService.getResume(resumeId);
		if (resume == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(resume);
	}

	// 이력서 수정 (완전 저장)
	@PutMapping("/{resumeId}")
	@PreAuthorize("@permission.isResumeOwner(#resumeId, authentication)")
	public ResponseEntity<?> editResume(@PathVariable Long resumeId,
										@Valid @RequestBody ResumeVO resumeVO,
										BindingResult bindingResult) {

		resumeVO.setId(resumeId);

		// 유효성 오류 처리
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(Map.of("success", false,
					"errors", bindingResult.getAllErrors()));
		}

		try {
			resumeService.editResume(resumeVO);
			return ResponseEntity.ok(Map.of("success", true, "id", resumeVO.getId()));

		} catch (ResumeNotFoundException e) {
			// 서비스에서 '이력서 없음 예외'가 발생
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

		} catch (RuntimeException e) {
			// 그 외 다른 예외가 발생
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	// 이력서 수정 (임시 저장)
	@PutMapping("/draft/{resumeId}")
	@PreAuthorize("@permission.isResumeOwner(#resumeId, authentication)")
	public ResponseEntity<?> editDraftResume(@PathVariable Long resumeId,
											 @RequestBody ResumeVO resumeVO) {

		resumeVO.setId(resumeId);

		try {
			resumeService.editResume(resumeVO);
			return ResponseEntity.ok(Map.of("success", true));

		} catch (ResumeNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	// 이력서 삭제 (소프트 삭제)
	@DeleteMapping("/{resumeId}")
	@PreAuthorize("@permission.isResumeOwner(#resumeId, authentication)")
	public ResponseEntity<Void> deleteResume(@PathVariable Long resumeId) {
		boolean result = resumeService.deleteResume(resumeId);

		// 삭제 결과가 0인 경우 404오류
		return result ? ResponseEntity.ok().build()
				: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
