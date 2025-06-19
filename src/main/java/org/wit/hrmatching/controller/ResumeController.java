package org.wit.hrmatching.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.wit.hrmatching.config.auth.CustomUserDetails;
import org.wit.hrmatching.service.ResumeService;
import org.wit.hrmatching.vo.ResumeVO;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ResumeController {

	private final ResumeService resumeService;

	@RequestMapping("/resume/register")
	public ModelAndView registerResume(@AuthenticationPrincipal CustomUserDetails userDetails) {

		Long userId = userDetails.getUser().getId();

		ModelAndView modelAndView = new ModelAndView("resume/resume_register");
		modelAndView.addObject("userId", userId);

		return modelAndView;
	}

	@RequestMapping("/resume/register_ok")
	public ModelAndView registerResumeOk(@ModelAttribute ResumeVO resumeVO) {

		// TODO: 등록 성공/실패에 따라 redirect 또는 에러 처리 분기
		int flag = resumeService.registerResume(resumeVO);

		ModelAndView modelAndView = new ModelAndView("resume/resume_register_ok");
		modelAndView.addObject("flag", flag);

		return modelAndView;
	}

	@RequestMapping("/resume/list")
	public ModelAndView listResume(@AuthenticationPrincipal CustomUserDetails userDetails) {

		Long userId = userDetails.getUser().getId();

		List<ResumeVO> resumeList = resumeService.selectResumeList(userId);

		ModelAndView modelAndView = new ModelAndView("resume/resume_list");
		modelAndView.addObject("resumeList", resumeList);

		return modelAndView;
	}

}
