package org.wit.hrmatching.controller.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wit.hrmatching.service.employer.TechStackService;
import org.wit.hrmatching.vo.*;
import org.wit.hrmatching.service.employer.JobPostService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employer")
public class JobPostController {

    private final JobPostService jobPostService;
    private final TechStackService techStackService;

//    채용공고 작성하기
    @GetMapping("/jobpost_write")
    public String postForm(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<String> stackList = techStackService.getAllStacks();
        model.addAttribute("stackList", stackList);
        model.addAttribute("jobPostVO", new JobPostVO());
        return "employer/jobpost/jobpost_form";
    }

    @PostMapping("/jobpost")
    public String  submitJobPost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @ModelAttribute JobPostVO jobPostVO,
                                 RedirectAttributes redirectAttributes) {

        Long userId = userDetails.getUser().getId();
        jobPostVO.setUserId(userId); // 직접 설정

        int flag = jobPostService.insertJobPost(jobPostVO);

        if (flag > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "채용공고가 성공적으로 등록되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "채용공고 등록에 실패했습니다.");
        }

        return "redirect:/employer/jobpost_list"; // 예시 리다이렉트 경로
    }

    // 채용 공고 관리 ( 페이징, 공고검색, 공고수 카운트 )
    @RequestMapping("/jobpost_list")
    public ModelAndView jobpost_list(@AuthenticationPrincipal CustomUserDetails userDetails,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(required = false) String keyword) {

        Long userId = userDetails.getUser().getId();
        int offset = (page - 1) * size;

        List<JobPostVO> jobPostList = jobPostService.getJobPostList(userId, keyword, offset, size);
        int totalCount = jobPostService.getJobPostCount(userId, keyword);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        JobPostVO cntVo = jobPostService.getJobPostStatusCounts(userId);

        ModelAndView mav = new ModelAndView("employer/jobpost/jobpost_list");
        mav.addObject("jobPostList", jobPostList);
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
        mav.addObject("totalCount", totalCount);
        mav.addObject("expiredCount", cntVo.getExpiredCount());
        mav.addObject("activeCount", cntVo.getActiveCount());
        mav.addObject("keyword", keyword); // 검색창 유지용

        return mav;
    }

//    선택 공고 jobPostId에 해당하는 공고 상세 정보 보기 (조회전용)
    @RequestMapping("/view") //-------------기업페이지 _ 이력서확인
    public ModelAndView selectJobPostview(@RequestParam Long jobPostId) {
        JobPostVO jobPostVO = jobPostService.selectJobPostDetail(jobPostId);

        ModelAndView modelAndView = new ModelAndView("employer/jobpost/view");
        modelAndView.addObject("post", jobPostVO);

        return modelAndView;
    }

//    공고 수정 폼 보여주기
    @RequestMapping("/jobpost_edit")
    public ModelAndView editJobPostDetail(@RequestParam Long jobPostId) {
        JobPostVO jobPostVO = jobPostService.selectJobPostDetail(jobPostId);
        ModelAndView modelAndView = new ModelAndView("employer/jobpost/jobpost_edit");
        modelAndView.addObject("jobPostVO", jobPostVO);
        return modelAndView;
    }

//    공고 수정요청 처리 수정된 내용 DB에 반영
    @PostMapping("/jobpost_edit")
    public String updateJobPost(@ModelAttribute JobPostVO jobPostVO,
                                RedirectAttributes redirectAttributes) {
        jobPostService.editJobPostDetail(jobPostVO);

        redirectAttributes.addFlashAttribute("successMessage", "공고가 수정되었습니다.");
        return "redirect:/employer/jobpost_list";
    }

//    여러 개의 공고 ID를 soft delete 처리
    @PostMapping("/jobpost_delete")  //----------------------------------- 채용공고 삭제
    public ResponseEntity<?> softDeleteSelectedJobPosts(@RequestBody List<Long> ids) {
        System.out.println("삭제 요청 ID들: " + ids);
        jobPostService.softDeleteJobPosts(ids);
        return ResponseEntity.ok("삭제 완료");
    }

    //    신규 채용공고 작성
//    @GetMapping("/jobpost")
//    public ModelAndView registerJobPost(@AuthenticationPrincipal CustomUserDetails userDetails,
//                                        @ModelAttribute JobPostVO jobPostVO) {
//
//        Long userId = userDetails.getUser().getId();
//        jobPostVO.setUserId(userId); // 직접 설정
//
//        int flag = jobPostService.insertJobPost(jobPostVO);
//
//        ModelAndView modelAndView = new ModelAndView("employer/jobpost/jobpost_list");
//        modelAndView.addObject("flag", flag);
//
//        return modelAndView;
//    }
}
