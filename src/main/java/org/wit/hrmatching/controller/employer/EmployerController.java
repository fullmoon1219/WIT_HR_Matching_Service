package org.wit.hrmatching.controller.employer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.wit.hrmatching.vo.ApplicantProfilesVO;
import org.wit.hrmatching.vo.CustomUserDetails;
import org.wit.hrmatching.service.JobPostService;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employer")
public class EmployerController {

    private final JobPostService jobPostService;

    @GetMapping("/jobpost") //---------------------------------신규 채용공고 작성
    public ModelAndView registerJobPost(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUser().getId();

        ModelAndView modelAndView = new ModelAndView("employer/jobpost/jobpost");
        modelAndView.addObject("userId", userId);

        return modelAndView;

    }

    @PostMapping("/jobpost/jobpost_ok") // -------------------------------------신규 채용공고 등록
    public ModelAndView registerResumeOk(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @ModelAttribute JobPostVO jobPostVO) {

        Long userId = userDetails.getUser().getId(); // 현재 로그인한 기업사용자 ID
        jobPostVO.setUserId(userId); // 직접 설정


        System.out.println("자료 넘어가는지 확인");

        int flag = jobPostService.registerJobPost(jobPostVO);

        ModelAndView modelAndView = new ModelAndView("employer/jobpost/jobpost_ok");
        modelAndView.addObject("flag", flag);

        return modelAndView;
    }

    @RequestMapping("/main") //-------------기업 페이지 첫화면
    public ModelAndView main(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        System.out.println("user_id: " + userId);
        List<JobPostVO> jobPostList = jobPostService.selectRecentJobPostList(userId); // create_at 기준으로 가장 최신 5개
        //List<ApplicantProfilesVO> draftResumeList = jobPostService.selectApplicantList(userId);


        ModelAndView modelAndView = new ModelAndView("employer/main");
        System.out.println(jobPostList.size());
        modelAndView.addObject("jobPostList", jobPostList);
        //modelAndView.addObject("draftResumeList", draftResumeList);

        return modelAndView;
    }

    @RequestMapping("/jobpost_list") //-------------기업페이지 _ 채용공고
    public ModelAndView jobpost_list(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        List<JobPostVO> jobPostList = jobPostService.selectJobPostAllList(userId);
        //List<ApplicantProfilesVO> draftResumeList = jobPostService.selectApplicantList(userId);


        ModelAndView modelAndView = new ModelAndView("employer/jobpost/jobpost_list");
        modelAndView.addObject("jobPostList", jobPostList);
        //modelAndView.addObject("draftResumeList", draftResumeList);

        return modelAndView;
    }

    @RequestMapping("/applications") //-------------기업페이지 _ 이력서확인
    public ModelAndView applications(HttpServletRequest request, Model model) {
        return new ModelAndView("employer/applications");
    }

    @RequestMapping("/profile") //-------------기업페이지 _ 이력서확인
    public ModelAndView profile(HttpServletRequest request, Model model) {
        return new ModelAndView("employer/profile");
    }

}
