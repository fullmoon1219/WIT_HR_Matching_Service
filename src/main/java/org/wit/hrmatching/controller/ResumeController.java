package org.wit.hrmatching.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.wit.hrmatching.config.auth.CustomUserDetails;
import org.wit.hrmatching.service.ResumeService;
import org.wit.hrmatching.vo.ResumeVO;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ResumeController {

	private final ResumeService resumeService;

	@GetMapping("/resume/register")
	public ModelAndView registerResume(@AuthenticationPrincipal CustomUserDetails userDetails) {

		Long userId = userDetails.getUser().getId();

		// TODO: 구직자 정보(사진 포함) 추가 → 구직자 메인페이지 로직 완료 후 반영 (작성 화면용)

		ModelAndView modelAndView = new ModelAndView("resume/resume_register");
		modelAndView.addObject("userId", userId);

		return modelAndView;
	}

	@PostMapping("/resume/register_ok")
	public ModelAndView registerResumeOk(@ModelAttribute ResumeVO resumeVO) {

		// TODO: 등록 성공/실패에 따라 redirect 또는 에러 처리 분기
		// TODO: 디자인 확정 후 선택형 필드 반영 예정 (작성 화면)
		int flag = resumeService.registerResume(resumeVO);

		ModelAndView modelAndView = new ModelAndView("resume/resume_register_ok");
		modelAndView.addObject("flag", flag);

		return modelAndView;
	}

	@GetMapping("/resume/list")
	public ModelAndView listResume(@AuthenticationPrincipal CustomUserDetails userDetails) {

		Long userId = userDetails.getUser().getId();

		List<ResumeVO> resumeList = resumeService.selectResumeList(userId);

		ModelAndView modelAndView = new ModelAndView("resume/resume_list");
		modelAndView.addObject("resumeList", resumeList);

		return modelAndView;
	}

	@GetMapping("/resume/view")
	public ModelAndView viewResume(@AuthenticationPrincipal CustomUserDetails userDetails,
								   @RequestParam Long resumeId) {

		Long userId = userDetails.getUser().getId();

		ResumeVO resumeVO = resumeService.viewResume(resumeId);

		System.out.println("로그인 유저 ID: " + userId);
		System.out.println("이력서 작성자 ID: " + resumeVO.getUserId());

		// 작성자와 로그인한 유저가 다를 경우
		// → AccessDeniedException 발생 → Spring Security가 403 응답 처리 → /error 로 포워딩
		if (resumeVO.getUserId() != userId) {
			throw new AccessDeniedException("접근 권한이 없습니다.");
		}

		// TODO: 구직자 정보(사진 포함) 추가 → 구직자 메인페이지 로직 완료 후 반영 (상세보기 화면용)

		ModelAndView modelAndView = new ModelAndView("resume/resume_view");
		modelAndView.addObject("resume", resumeVO);

		return modelAndView;
	}

}
