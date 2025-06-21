package org.wit.hrmatching.controller.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/applicant/resume")
public class ResumeController {

	private final ResumeService resumeService;

	@GetMapping("/register")
	public ModelAndView registerResume(@AuthenticationPrincipal CustomUserDetails userDetails) {

		Long userId = userDetails.getUser().getId();

		// TODO: 구직자 정보(사진 포함) 추가 → 구직자 메인페이지 로직 완료 후 반영 (작성 화면용)

		ModelAndView modelAndView = new ModelAndView("resume/resume_register");
		modelAndView.addObject("userId", userId);

		return modelAndView;
	}

	@PostMapping("/register_ok")
	public ModelAndView registerResumeOk(@AuthenticationPrincipal CustomUserDetails userDetails,
										 @ModelAttribute ResumeVO resumeVO) {

		Long userId = userDetails.getUser().getId();
		resumeVO.setUserId(userId);

		// TODO: 등록 성공/실패에 따라 redirect 또는 에러 처리 분기
		// TODO: 디자인 확정 후 선택형 필드 반영 예정 (작성 화면)
		int flag = resumeService.registerResume(resumeVO);

		ModelAndView modelAndView = new ModelAndView("resume/resume_register_ok");
		modelAndView.addObject("flag", flag);

		return modelAndView;
	}

	@GetMapping("/list")
	public ModelAndView listResume(@AuthenticationPrincipal CustomUserDetails userDetails) {

		Long userId = userDetails.getUser().getId();

		List<ResumeVO> resumeList = resumeService.getResumeList(userId);

		ModelAndView modelAndView = new ModelAndView("resume/resume_list");
		modelAndView.addObject("resumeList", resumeList);

		return modelAndView;
	}

	@GetMapping("/view")
	@PreAuthorize("@permission.isResumeOwner(#id, authentication)")
	public ModelAndView viewResume(@RequestParam Long resumeId) {

		ResumeVO resumeVO = resumeService.getResume(resumeId);

		// TODO: 구직자 정보(사진 포함) 추가 → 구직자 메인페이지 로직 완료 후 반영 (상세보기 화면용)

		ModelAndView modelAndView = new ModelAndView("resume/resume_view");
		modelAndView.addObject("resume", resumeVO);

		return modelAndView;
	}

	@GetMapping("/edit")
	@PreAuthorize("@permission.isResumeOwner(#id, authentication)")
	public ModelAndView editResume(@RequestParam Long resumeId) {

		ResumeVO resumeVO = resumeService.getResumeForUpdate(resumeId);

		ModelAndView modelAndView = new ModelAndView("resume/resume_edit");
		modelAndView.addObject("resume", resumeVO);

		return modelAndView;
	}

	@PostMapping("/edit_ok")
	@PreAuthorize("@permission.isResumeOwner(#id, authentication)")
	public ModelAndView editResumeOk(@ModelAttribute ResumeVO resumeVO) {

		int flag = resumeService.editResume(resumeVO);

		ModelAndView modelAndView = new ModelAndView("resume/resume_edit_ok");
		modelAndView.addObject("flag", flag);

		return modelAndView;
	}
}
