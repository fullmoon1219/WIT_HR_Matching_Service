package org.wit.hrmatching.controller.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.service.applicant.ApplicationService;
import org.wit.hrmatching.vo.application.ApplicationDetailVO;
import org.wit.hrmatching.vo.user.CustomUserDetails;
import org.wit.hrmatching.vo.applicantPaging.PageResponseVO;
import org.wit.hrmatching.vo.applicantPaging.SearchCriteria;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
public class ApplicationRestController {

	private final ApplicationService applicationService;

	@GetMapping("")
	public ResponseEntity<?> getApplicationList(@AuthenticationPrincipal CustomUserDetails userDetails,
												@ModelAttribute SearchCriteria criteria) {

		long userId = userDetails.getId();

		PageResponseVO<ApplicationDetailVO> responseVO = applicationService.getApplicationList(userId, criteria);

		return ResponseEntity.ok(responseVO);
	}

	@GetMapping("/{applicationId}")
	public ResponseEntity<?> getApplication(@PathVariable long applicationId) {

		return ResponseEntity.ok(applicationService.getApplication(applicationId));
	}
}
