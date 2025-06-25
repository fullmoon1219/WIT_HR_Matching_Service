package org.wit.hrmatching.controller.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.wit.hrmatching.service.applicant.RecruitService;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;

@Controller
@RequiredArgsConstructor
@RequestMapping("/applicant/recruit")
public class RecruitController {

	private final RecruitService recruitService;

	@GetMapping("/view")
	public ModelAndView viewRecruit(){//@RequestParam long recruitId) {

		JobPostVO jobPostVO = recruitService.viewRecruit(1);

		if (jobPostVO == null) {
			return new ModelAndView("error/not-found");
		}

		EmployerProfilesVO employerProfilesVO = recruitService.viewEmployerProfile(jobPostVO.getUserId());

		ModelAndView modelAndView = new ModelAndView("applicant/recruit/view");
		modelAndView.addObject("recruit", jobPostVO);
		modelAndView.addObject("employerProfiles", employerProfilesVO);

		return modelAndView;
	}
}
