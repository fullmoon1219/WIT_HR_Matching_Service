package org.wit.hrmatching.controller.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recruit")
public class RecruitController {

	@Value("${kakao.api-key}")
	private String kakaoApiKey;

	@GetMapping("/view/{jobPostId}")
	public String viewRecruit(Model model) {
		model.addAttribute("kakaoApiKey", kakaoApiKey);
		return "applicant/recruit/view";
	}

	@GetMapping("/list")
	public String listRecruit() {
		return "applicant/recruit/list";
	}
}
