package org.wit.hrmatching.controller.applicant;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/applicant/applications")
public class ApplicationController {

	@GetMapping
	public String applicationList() {
		return "applicant/application/list";
	}

	@GetMapping("/apply/{jobPostId}")
	public String applyRecruit() {
		return "applicant/application/apply";
	}
}
