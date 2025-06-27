package org.wit.hrmatching.controller.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/applicant/application")
public class ApplicationController {

	@GetMapping
	public String applicationList() {
		return "applicant/application/list";
	}
}
