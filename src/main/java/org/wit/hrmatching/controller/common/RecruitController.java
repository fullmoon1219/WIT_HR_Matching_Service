package org.wit.hrmatching.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recruit")
public class RecruitController {

	@GetMapping("/view/{jobPostId}")
	public String viewRecruit() {
		return "applicant/recruit/view";
	}

	@GetMapping("/list")
	public String listRecruit() {
		return "applicant/recruit/list";
	}
}
