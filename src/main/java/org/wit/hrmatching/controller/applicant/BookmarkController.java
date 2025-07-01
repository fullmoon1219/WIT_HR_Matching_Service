package org.wit.hrmatching.controller.applicant;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/applicant/bookmarks")
public class BookmarkController {

	@GetMapping("")
	public String bookmarkList() {

		return "applicant/bookmark/list";
	}
}
