package org.wit.hrmatching.controller.applicant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.service.applicant.ResumeService;
import org.wit.hrmatching.vo.CustomUserDetails;
import org.wit.hrmatching.vo.ResumeVO;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes")
public class ResumeRestController {

	private final ResumeService resumeService;

	// TODO: 구직자 정보(사진 포함) 추가 → 구직자 메인페이지 로직 완료 후 반영 (작성 화면용)

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

	// 대표 이력서 조회
	@GetMapping("/public")
	public ResponseEntity<ResumeVO> getPublicResume(@AuthenticationPrincipal CustomUserDetails userDetails) {
		ResumeVO resume = resumeService.getPublicResume(userDetails.getId());
		return resume != null ? ResponseEntity.ok(resume)
				: ResponseEntity.noContent().build();
	}

	// 작성 완료된 이력서 목록 조회
	@GetMapping
	public List<ResumeVO> getResumeList(@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getId();
		return resumeService.getResumeList(userId);
	}

	// 임시 저장 이력서 목록 조회
	@GetMapping("/draft")
	public List<ResumeVO> getDraftResumeList(@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getId();
		return resumeService.getDraftResumeList(userId);
	}

	// 대표 이력서 설정
	@PutMapping("/{resumeId}/public")
	@PreAuthorize("@permission.isResumeOwner(#resumeId, authentication)")
	public ResponseEntity<?> setResumePublic(@PathVariable Long resumeId,
											 @AuthenticationPrincipal CustomUserDetails userDetails) {

		boolean result = resumeService.setResumeAsPublic(resumeId, userDetails.getId());

		return result ? ResponseEntity.ok().build()
				: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
