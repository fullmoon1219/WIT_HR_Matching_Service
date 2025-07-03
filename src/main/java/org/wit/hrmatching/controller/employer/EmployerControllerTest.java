package org.wit.hrmatching.controller.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employer/test")
@RequiredArgsConstructor
public class EmployerControllerTest {

    @GetMapping("/main")
    public String profile() {

        return "employer/mypage/profile";
    }

    @GetMapping("/postList")
    public String postList() {

        return "employer/mypage/post-list";
    }

    @GetMapping("/write")
    public String postForm() {

        return "employer/mypage/post-form";
    }

    @GetMapping("/view")
    public String postView() {

        return "employer/mypage/post-view";
    }

    @GetMapping("/modify")
    public String postModify() {

        return "employer/mypage/post-modify";
    }

    @GetMapping("/test/applicantList")
    public String postApplicant() {

        return "employer/mypage/applicant-list";
    }

    @GetMapping("/test/applicantDetail")
    public String postApplicantDetail() {

        return "employer/mypage/applicant-detail";
    }
}
