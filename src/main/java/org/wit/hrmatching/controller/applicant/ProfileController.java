package org.wit.hrmatching.controller.applicant;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/applicant")
public class ProfileController {

	@GetMapping("/main")
	public String main() {
		return "applicant/mypage/main";
	}

	@GetMapping("/profile")
	public String profile() {
		return "applicant/mypage/profile";
	}
}
