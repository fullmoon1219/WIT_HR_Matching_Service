package org.wit.hrmatching.controller.employer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.wit.hrmatching.vo.*;
import org.wit.hrmatching.service.employer.JobPostService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employer")
public class JobPostController {

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

    @RequestMapping("/view") //-------------기업페이지 _ 이력서확인
    public ModelAndView selectJobPostview(@RequestParam Long jobPostId) {
        JobPostVO jobPostVO = jobPostService.selectJobPostDetail(jobPostId);


        ModelAndView modelAndView = new ModelAndView("employer/jobpost/view");
        modelAndView.addObject("post", jobPostVO);

        return modelAndView;
    }

    @RequestMapping("/jobpost_edit") //-------------기업페이지 _ 이력서확인
    public ModelAndView editJobPostDetail(@RequestParam Long jobPostId) {
        JobPostVO jobPostVO = jobPostService.selectJobPostDetail(jobPostId);


        ModelAndView modelAndView = new ModelAndView("employer/jobpost/jobpost_edit");
        modelAndView.addObject("jobPostVO", jobPostVO);

        return modelAndView;
    }

    @PostMapping("/jobpost_edit_ok")
    public ModelAndView editJobPostDetail(
                                     @ModelAttribute JobPostVO jobPostVO
                                     ) {

        boolean result;
        ModelAndView modelAndView = new ModelAndView();
            // 입력값 유효성 검사
            // 만약 입력값이 모두 입력되지 않으면 에러메세지와 함께 작성 페이지로.
//            if (bindingResult.hasErrors()) {
//                modelAndView.setViewName("employer/jobpost/jobpost_edit");
//                modelAndView.addObject("jobPostVO", jobPostVO);
//                modelAndView.addObject("org.springframework.validation.BindingResult.jobPostVO", bindingResult);
//                return modelAndView;
//            }

            // 정식 등록: is_completed = true
            jobPostVO.setCompleted(true);
            result = jobPostService.editJobPostDetail(jobPostVO);

        if (result) {
            modelAndView.setViewName("employer/jobpost/jobpost_edit_ok");
            modelAndView.addObject("jobPostId", jobPostVO.getId());
        } else {
            modelAndView.setViewName("error/db-access-denied");
        }

        return modelAndView;
    }

    @PostMapping("/delete")
    public ModelAndView deleteJobPostDetail(@RequestParam Long jobPostId) {

        boolean result = jobPostService.deleteJobPost(jobPostId);
        return new ModelAndView(result ? "redirect:/employer/jobpost_list" : "error/db-access-denied");
    }

    @RequestMapping("/applications") //-------------기업페이지 _ 이력서확인
    public ModelAndView applications(HttpServletRequest request, Model model) {
        return new ModelAndView("employer/applications");
    }

}
