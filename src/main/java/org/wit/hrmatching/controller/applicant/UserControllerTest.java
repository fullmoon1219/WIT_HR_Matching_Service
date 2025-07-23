package org.wit.hrmatching.controller.applicant;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users/mypage")   // 💡 여기서 URL prefix 지정
public class UserControllerTest {

    @GetMapping("/main")           // 💡 결과적으로 /users/mypage/main으로 매핑됨
    public String showMain() {
        return "/applicant/users/mypage";       // 💡 반환할 뷰 이름
    }

    @GetMapping("/request")
    public String showApplicationHistory() {
        return "/applicant/users/mypage_request";
    }

    @GetMapping("/resume")
    public String showApplicationResume() {
        return "/applicant/users/mypage_application_resume";
    }

    @GetMapping("/list")
    public String showApplicationlist() {
        return "/applicant/users/mypage_application_list";
    }

    @GetMapping("/qa")
    public String showApplicationqa() { return "/applicant/users/mypage_qa"; }

    @GetMapping("/ai")
    public String showApplicationAi() { return "/applicant/users/ai-test"; }

    @GetMapping("/interview")
    public String showApplicationInterview() { return "/applicant/users/ai-interview"; }

    @GetMapping("/feedback")
    public String showApplicationFeedback() { return "/applicant/users/ai-feedback"; }
}



