package org.wit.hrmatching.controller.applicant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.wit.hrmatching.enums.ResumeAction;
import org.wit.hrmatching.vo.CustomUserDetails;
import org.wit.hrmatching.service.applicant.ResumeService;
import org.wit.hrmatching.vo.ResumeVO;

import java.util.List;

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
