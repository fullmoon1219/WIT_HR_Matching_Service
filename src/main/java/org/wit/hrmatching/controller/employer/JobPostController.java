package org.wit.hrmatching.controller.employer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wit.hrmatching.vo.*;
import org.wit.hrmatching.service.employer.JobPostService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employer")
public class JobPostController {

    private final JobPostService jobPostService;

    @GetMapping("/jobpost") //---------------------------------신규 채용공고 작성
    public ModelAndView registerJobPost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @ModelAttribute JobPostVO jobPostVO) {

        Long userId = userDetails.getUser().getId();
        jobPostVO.setUserId(userId); // 직접 설정

        int flag = jobPostService.registerJobPost(jobPostVO);

        ModelAndView modelAndView = new ModelAndView("employer/jobpost/jobpost_list");
        modelAndView.addObject("flag", flag);

        return modelAndView;
    }

    @PostMapping("/jobpost") //---------------------------------신규 채용공고 작성
    public String  submitJobPost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @ModelAttribute JobPostVO jobPostVO,
                                      RedirectAttributes redirectAttributes) {

        Long userId = userDetails.getUser().getId();
        jobPostVO.setUserId(userId); // 직접 설정

        int flag = jobPostService.registerJobPost(jobPostVO);

        if (flag > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "채용공고가 성공적으로 등록되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "채용공고 등록에 실패했습니다.");
        }

        return "redirect:/employer/jobpost_list"; // 예시 리다이렉트 경로
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

    @RequestMapping("/jobpost_edit")
    public ModelAndView editJobPostDetail(@RequestParam Long jobPostId) {
        JobPostVO jobPostVO = jobPostService.selectJobPostDetail(jobPostId);
        ModelAndView modelAndView = new ModelAndView("employer/jobpost/jobpost_edit");
        modelAndView.addObject("jobPostVO", jobPostVO);
        return modelAndView;
    }

    @PostMapping("/jobpost_edit")
    public String updateJobPost(@ModelAttribute JobPostVO jobPostVO,
                                RedirectAttributes redirectAttributes) {
        jobPostService.editJobPostDetail(jobPostVO);

        redirectAttributes.addFlashAttribute("successMessage", "공고가 수정되었습니다.");
        return "redirect:/employer/jobpost_list";
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
