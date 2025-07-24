package org.wit.hrmatching.controller.applicant;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/applicant")
public class ApplicantProfileController {

	@GetMapping("/main")
	public String main() {
		return "applicant/mypage/main";
	}

	@GetMapping("/profile")
	public String profile() {
		return "applicant/mypage/profile";
	}

	@GetMapping("/request")
	public String request() {
		return "/applicant/users/mypage_request";
	}


	@GetMapping("/qa/{inquiryId}")
	public String QA(@PathVariable String inquiryId) {
		return "/applicant/users/mypage_qa";
	}
}
