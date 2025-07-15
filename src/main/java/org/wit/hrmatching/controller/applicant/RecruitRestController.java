package org.wit.hrmatching.controller.applicant;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.exception.DuplicateApplicationException;
import org.wit.hrmatching.service.applicant.BookmarkService;
import org.wit.hrmatching.service.applicant.RecruitService;
import org.wit.hrmatching.service.applicant.ResumeService;
import org.wit.hrmatching.vo.applicantPaging.PageResponseVO;
import org.wit.hrmatching.vo.applicantPaging.SearchCriteria;
import org.wit.hrmatching.vo.application.ApplicationsVO;
import org.wit.hrmatching.vo.job.JobPostVO;
import org.wit.hrmatching.vo.job.RecruitListVO;
import org.wit.hrmatching.vo.user.CustomUserDetails;
import org.wit.hrmatching.vo.user.EmployerProfilesVO;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruit")
public class RecruitRestController {

	private final RecruitService recruitService;
	private final ResumeService resumeService;
	private final BookmarkService bookmarkService;

	@GetMapping("/{jobPostId}")
	public ResponseEntity<Map<String, Object>> getRecruit(@PathVariable long jobPostId,
														  HttpSession session,
														  @AuthenticationPrincipal CustomUserDetails userDetails) {

		// 조회수 중복 제한 (10분)
		String key = "viewed_" + jobPostId;
		Long lastViewTime = (Long) session.getAttribute(key);
		long now = System.currentTimeMillis();

		if (lastViewTime == null || now - lastViewTime > 600_000) {
			recruitService.increaseViewCount(jobPostId);
			session.setAttribute(key, now);
		}

		JobPostVO jobPost = recruitService.viewRecruit(jobPostId);
		if (jobPost == null) {
			return ResponseEntity.notFound().build();
		}

		EmployerProfilesVO employer = recruitService.viewEmployerProfile(jobPost.getUserId());

		boolean isApplied = false;
		boolean isBookmarked = false;

		if (userDetails != null) {
			long userId = userDetails.getId();

			isApplied = recruitService.isApplicationExist(userId, jobPostId);
			isBookmarked = bookmarkService.checkBookmarkExist(userId, jobPostId);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("jobPost", jobPost);
		response.put("isApplied", isApplied);
		response.put("isBookmarked", isBookmarked);

		if (employer != null) {
			response.put("employer", employer);
		} else {
			response.put("employer", new EmployerProfilesVO());
		}

		Map<String, Object> userInfo = new HashMap<>();
		if (userDetails != null) {
			// 사용자가 로그인한 경우
			userInfo.put("isLoggedIn", true);
			userInfo.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
		} else {
			// 사용자가 로그인하지 않은 경우
			userInfo.put("isLoggedIn", false);
			userInfo.put("role", null);
		}
		response.put("userInfo", userInfo);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{jobPostId}/summary")
	public ResponseEntity<Map<String, Object>> getJobPostSummary(@PathVariable long jobPostId,
																 @AuthenticationPrincipal CustomUserDetails userDetails) {

		Map<String, Object> summary = recruitService.getJobPostSummary(jobPostId);
		if (summary == null) {
			return ResponseEntity.notFound().build();
		}

		boolean isApplied = false;
		boolean isProfileComplete = false;

		if (userDetails != null) {
			long userId = userDetails.getId();

			isApplied = recruitService.isApplicationExist(userId, jobPostId);
			isProfileComplete = resumeService.confirmProfile(userId);
		}

		summary.put("isApplied", isApplied);
		summary.put("isProfileComplete", isProfileComplete);

		return ResponseEntity.ok(summary);
	}

	@PostMapping("/{jobPostId}")
	@PreAuthorize("@permission.isResumeOwner(#application.resumeId, authentication)")
	public ResponseEntity<?> applyRecruit(@PathVariable long jobPostId,
										  @RequestBody ApplicationsVO application,
										  @AuthenticationPrincipal CustomUserDetails userDetails) {

		long userId = userDetails.getId();
		long resumeId = application.getResumeId();

		try {
			recruitService.applyApplication(userId, jobPostId, resumeId);
			return ResponseEntity.ok().build();

		} catch (DuplicateApplicationException e) {
			// 서비스에서 '중복 지원 예외'가 발생
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

		} catch (RuntimeException e) {
			// 그 외 다른 예외(DB 오류 등)가 발생
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("")
	public ResponseEntity<?> getRecruitList(@ModelAttribute SearchCriteria criteria) {

		PageResponseVO<RecruitListVO> responseVO = recruitService.getRecruitList(criteria);

		return ResponseEntity.ok(responseVO);
	}
}
