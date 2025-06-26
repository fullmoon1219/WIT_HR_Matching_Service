package org.wit.hrmatching.controller.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.wit.hrmatching.service.applicant.RecruitService;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;

@Controller
@RequestMapping("/applicant/recruit")
public class RecruitController {

	@GetMapping("/view/{jobPostId}")
	public String viewRecruit() {
		return "applicant/recruit/view";
	}

	@GetMapping("/apply/{jobPostId}")
	public String applyRecruit() {
		return "applicant/recruit/apply";
	}
}
