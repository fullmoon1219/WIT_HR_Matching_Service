package org.wit.hrmatching.controller.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.wit.hrmatching.service.employer.EmpApplicationService;
import org.wit.hrmatching.service.employer.JobPostService;
import org.wit.hrmatching.service.employer.ProfileService;
import org.wit.hrmatching.vo.CustomUserDetails;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.EmployerRecentApplicantVO;
import org.wit.hrmatching.vo.JobPostVO;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/employer/application")
public class EmpApplicationController {
    private final EmpApplicationService empApplicationService;
    private final ProfileService profileService;
    private final JobPostService jobPostService;

    @GetMapping("/list")
    public ModelAndView postApplicant(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        List<EmployerRecentApplicantVO> vo = profileService.selectEmployerRecentApplicantList(userId);

        ModelAndView modelAndView = new ModelAndView("employer/empApplication/list"); // 뷰 이름 지정
        modelAndView.addObject("ApplicantList", vo); // model.addAttribute 와 같음

        return modelAndView;
    }

    @GetMapping("/resume_detail")
    public String postApplicantDetail() {

        return "employer/empApplication/resume_detail";
    }

    @GetMapping("/jobpost_detail")
    public ModelAndView showJobPostDetail(@RequestParam("postId") Long postId) {
        JobPostVO jobPostVO = jobPostService.selectJobPostDetail(postId);

        ModelAndView modelAndView = new ModelAndView("employer/empApplication/jobpost_detail"); // 💡 실제 템플릿 경로 맞게 수정
        modelAndView.addObject("jobPost", jobPostVO); // Thymeleaf에서 ${jobPost.xxx}로 접근 가능

        return modelAndView;
    }

}
