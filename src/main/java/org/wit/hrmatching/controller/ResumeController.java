package org.wit.hrmatching.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.wit.hrmatching.service.ResumeService;
import org.wit.hrmatching.vo.ResumeVO;

@Controller
@RequiredArgsConstructor
public class ResumeController {

	private final ResumeService resumeService;

	@RequestMapping("/resume/register")
	public ModelAndView registerResume() {

		ModelAndView modelAndView = new ModelAndView("resume/resume_register");

		return modelAndView;
	}

	@RequestMapping("/resume/register_ok")
	public ModelAndView registerResumeOk(@ModelAttribute ResumeVO resumeVO) {

		int flag = resumeService.registerResume(resumeVO);

		ModelAndView modelAndView = new ModelAndView("resume/resume_register_ok");
		modelAndView.addObject("flag", flag);

		return modelAndView;
	}

}
