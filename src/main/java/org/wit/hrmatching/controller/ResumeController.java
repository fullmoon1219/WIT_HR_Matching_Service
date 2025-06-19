package org.wit.hrmatching.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.wit.hrmatching.service.ResumeService;
import org.wit.hrmatching.vo.ResumeVO;

@Controller
public class ResumeController {

	@Autowired
	private ResumeService resumeService;

	@RequestMapping("/resume")
	public ModelAndView registerResume() {

		ModelAndView modelAndView = new ModelAndView("resume_register");

		return modelAndView;
	}

	@RequestMapping("/resume_ok")
	public ModelAndView registerResumeOk(@ModelAttribute ResumeVO resumeVO) {

		int flag = resumeService.registerResume(resumeVO);

		ModelAndView modelAndView = new ModelAndView("resume_register_ok");
		modelAndView.addObject("flag", flag);

		return modelAndView;
	}

}
