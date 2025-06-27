package org.wit.hrmatching.controller.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wit.hrmatching.service.applicant.ApplicationService;
import org.wit.hrmatching.vo.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
public class ApplicationRestController {

	private final ApplicationService applicationService;

	@GetMapping("")
	public ResponseEntity<?> getApplicationList(@AuthenticationPrincipal CustomUserDetails userDetails) {

		long userId = userDetails.getId();

		return ResponseEntity.ok(applicationService.getApplicationList(userId));
	}

	@GetMapping("/{applicationId}")
	public ResponseEntity<?> getApplication(@PathVariable long applicationId) {

		return ResponseEntity.ok(applicationService.getApplication(applicationId));
	}
}
