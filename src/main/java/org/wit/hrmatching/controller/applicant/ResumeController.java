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
@RequiredArgsConstructor
@RequestMapping("/applicant/resume")
public class ResumeController {

	private final ResumeService resumeService;

	@GetMapping("/register")
	public ModelAndView registerResume() {

		// TODO: 구직자 정보(사진 포함) 추가 → 구직자 메인페이지 로직 완료 후 반영 (작성 화면용)

		ModelAndView modelAndView = new ModelAndView("applicant/resume/register");
		modelAndView.addObject("resumeVO", new ResumeVO());

		return modelAndView;
	}

	@PostMapping("/register_ok")
	public ModelAndView registerResumeOk(@AuthenticationPrincipal CustomUserDetails userDetails,
										 @RequestParam("action")ResumeAction action,
										 @Valid @ModelAttribute ResumeVO resumeVO,
										 BindingResult bindingResult) {

		Long userId = userDetails.getUser().getId();
		resumeVO.setUserId(userId);

		// TODO: 등록 성공/실패에 따라 redirect 또는 에러 처리 분기
		// TODO: 디자인 확정 후 선택형 필드 반영 예정 (작성 화면)

		boolean result;
		ModelAndView modelAndView = new ModelAndView();

		if (action == ResumeAction.DRAFT) {

			// 임시 저장: is_completed = false
			resumeVO.setCompleted(false);
			result = resumeService.registerResume(resumeVO);

		} else if (action == ResumeAction.REGISTER) {

			// 입력값 유효성 검사
			// 만약 입력값이 모두 입력되지 않으면 에러메세지와 함께 작성 페이지로.
			if (bindingResult.hasErrors()) {
				modelAndView.setViewName("applicant/resume/register");
				modelAndView.addObject("resumeVO", resumeVO);
				modelAndView.addObject("org.springframework.validation.BindingResult.resumeVO", bindingResult);
				return modelAndView;
			}

			// 정식 등록: is_completed = true
			resumeVO.setCompleted(true);
			result = resumeService.registerResume(resumeVO);

		} else {
			// 잘못된 요청 처리 (개선 필요)
			result = false;
		}

		if (result) {
			modelAndView.setViewName("applicant/resume/register_ok");
			modelAndView.addObject("resumeId", resumeVO.getId());
		} else {
			modelAndView.setViewName("error/db-access-denied");
		}

		return modelAndView;
	}

	@GetMapping("/list")
	public ModelAndView listResume(@AuthenticationPrincipal CustomUserDetails userDetails) {

		Long userId = userDetails.getUser().getId();

		List<ResumeVO> resumeList = resumeService.getResumeList(userId);
		List<ResumeVO> draftResumeList = resumeService.getDraftResumeList(userId);

		ModelAndView modelAndView = new ModelAndView("applicant/resume/list");
		modelAndView.addObject("resumeList", resumeList);
		modelAndView.addObject("draftResumeList", draftResumeList);

		return modelAndView;
	}

	@GetMapping("/view")
	@PreAuthorize("@permission.isResumeOwner(#resumeId, authentication)")
	public ModelAndView viewResume(@RequestParam Long resumeId) {

		ResumeVO resumeVO = resumeService.getResume(resumeId);

		// TODO: 구직자 정보(사진 포함) 추가 → 구직자 메인페이지 로직 완료 후 반영 (상세보기 화면용)

		ModelAndView modelAndView = new ModelAndView("applicant/resume/view");
		modelAndView.addObject("resume", resumeVO);

		return modelAndView;
	}

	@GetMapping("/edit")
	@PreAuthorize("@permission.isResumeOwner(#resumeId, authentication)")
	public ModelAndView editResume(@RequestParam Long resumeId) {

		ResumeVO resumeVO = resumeService.getResumeForUpdate(resumeId);

		ModelAndView modelAndView = new ModelAndView("applicant/resume/edit");
		modelAndView.addObject("resumeVO", resumeVO);

		return modelAndView;
	}

	@PostMapping("/edit_ok")
	@PreAuthorize("@permission.isResumeOwner(#resumeVO.id, authentication)")
	public ModelAndView editResumeOk(@RequestParam("action") ResumeAction action,
									 @Valid @ModelAttribute ResumeVO resumeVO,
									 BindingResult bindingResult) {

		boolean result;
		ModelAndView modelAndView = new ModelAndView();

		if (action == ResumeAction.DRAFT) {

			// 임시 저장: is_completed = false
			resumeVO.setCompleted(false);
			result = resumeService.editResume(resumeVO);

		} else if (action == ResumeAction.REGISTER) {

			// 입력값 유효성 검사
			// 만약 입력값이 모두 입력되지 않으면 에러메세지와 함께 작성 페이지로.
			if (bindingResult.hasErrors()) {
				modelAndView.setViewName("applicant/resume/edit");
				modelAndView.addObject("resumeVO", resumeVO);
				modelAndView.addObject("org.springframework.validation.BindingResult.resumeVO", bindingResult);
				return modelAndView;
			}

			// 정식 등록: is_completed = true
			resumeVO.setCompleted(true);
			result = resumeService.editResume(resumeVO);

		} else {
			// 잘못된 요청 처리 (개선 필요)
			result = false;
		}

		if (result) {
			modelAndView.setViewName("applicant/resume/edit_ok");
			modelAndView.addObject("resumeId", resumeVO.getId());
		} else {
			modelAndView.setViewName("error/db-access-denied");
		}

		return modelAndView;
	}

	@PostMapping("/delete")
	@PreAuthorize("@permission.isResumeOwner(#resumeId, authentication)")
	public ModelAndView deleteResume(@RequestParam Long resumeId) {

		boolean result = resumeService.deleteResume(resumeId);

		return new ModelAndView(result ? "redirect:/applicant/resume/list" : "error/db-access-denied");
	}
}
