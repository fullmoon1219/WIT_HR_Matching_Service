package org.wit.hrmatching.controller.applicant;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.service.applicant.RecruitService;
import org.wit.hrmatching.service.applicant.ResumeService;
import org.wit.hrmatching.vo.ApplicationsVO;
import org.wit.hrmatching.vo.CustomUserDetails;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruit")
public class RecruitRestController {

	private final RecruitService recruitService;
	private final ResumeService resumeService;

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

		if (userDetails != null) {
			long userId = userDetails.getId();

			isApplied = recruitService.isApplicationExist(userId, jobPostId);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("jobPost", jobPost);
		response.put("isApplied", isApplied);

		if (employer != null) {
			response.put("employer", employer);
		} else {
			response.put("employer", new EmployerProfilesVO());
		}

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

		int result = recruitService.applyApplication(userId, jobPostId, resumeId);

		if (result == 0) {
			return ResponseEntity.ok().build();
		} else if (result == 1) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
