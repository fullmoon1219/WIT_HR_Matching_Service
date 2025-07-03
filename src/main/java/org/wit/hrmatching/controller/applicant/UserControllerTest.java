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

    @GetMapping("/history")
    public String showApplicationHistory() {
        return "/applicant/users/mypage_application_history";
    }

    @GetMapping("/resume")
    public String showApplicationResume() {
        return "/applicant/users/mypage_application_resume";
    }
}



