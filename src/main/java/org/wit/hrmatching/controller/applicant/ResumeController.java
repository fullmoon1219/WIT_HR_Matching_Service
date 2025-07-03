package org.wit.hrmatching.controller.applicant;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/applicant/resume")
public class ResumeController {

	@GetMapping("/register")
	public String registerResume() {
		return "applicant/resume/register";
	}

	@GetMapping("/list")
	public String listResume() {
		return "applicant/resume/list";
	}

	@GetMapping("/view/{resumeId}")
	public String viewResume() {
		return "applicant/resume/view";
	}

	@GetMapping("/edit/{resumeId}")
	public String editResume() {
		return "applicant/resume/edit";
	}
}
