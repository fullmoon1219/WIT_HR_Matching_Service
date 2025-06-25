package org.wit.hrmatching.controller.applicant;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.service.applicant.RecruitService;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruit")
public class RecruitRestController {

	private final RecruitService recruitService;

	@GetMapping("/{jobPostId}")
	public ResponseEntity<Map<String, Object>> getRecruit(@PathVariable long jobPostId,
														  HttpSession session) {

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

		Map<String, Object> response = new HashMap<>();
		response.put("employer", employer);
		response.put("jobPost", jobPost);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{jobPostId}/summary")
	public ResponseEntity<Map<String, Object>> getJobPostSummary(@PathVariable long jobPostId) {

		Map<String, Object> summary = recruitService.getJobPostSummary(jobPostId);

		return ResponseEntity.ok(summary);
	}

}
