package org.wit.hrmatching.controller.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wit.hrmatching.service.employer.ProfileService;
import org.wit.hrmatching.service.employer.TechStackService;
import org.wit.hrmatching.service.employer.JobPostService;
import org.wit.hrmatching.vo.job.JobPostVO;
import org.wit.hrmatching.vo.job.TechStackVO;
import org.wit.hrmatching.vo.user.CustomUserDetails;
import org.wit.hrmatching.vo.user.EmployerProfilesVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employer")
public class JobPostController {

    private final JobPostService jobPostService;
    private final ProfileService profileService;
    private final TechStackService techStackService;

//    채용공고 작성하기
    @GetMapping("/jobpost_write")
    public String postForm(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<TechStackVO> stackList = techStackService.getAllStacks();
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

    //기업페이지 공고 보기 (공고 + 기업정보 + 지도(지도는 구현 전))
    @RequestMapping("/jobpost_view")
    public ModelAndView selectJobPostview(@RequestParam Long jobPostId) {
        JobPostVO jobPostVO = jobPostService.selectJobPostDetail(jobPostId);
        EmployerProfilesVO eVO = profileService.getEmployerProfile(jobPostVO.getUserId());

        List<String> selectedStackNames = new ArrayList<>();

        String techStacks = jobPostVO.getRequiredSkills();
        if (techStacks != null && !techStacks.trim().isEmpty()) {
            List<Long> stackIds = Arrays.stream(techStacks.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            // id 리스트로 name 목록 조회
            selectedStackNames = techStackService.getStackNamesByIds(stackIds);
        }

        ModelAndView modelAndView = new ModelAndView("employer/jobpost/jobpost_view");
        modelAndView.addObject("post", jobPostVO);
        modelAndView.addObject("employerProfile", eVO);
        modelAndView.addObject("selectedStacks", selectedStackNames);

        return modelAndView;
    }


//    공고 수정 폼 보여주기
    @RequestMapping("/jobpost_edit")
    public ModelAndView editJobPostDetail(@RequestParam Long jobPostId) {
        JobPostVO jobPostVO = jobPostService.selectJobPostDetail(jobPostId);
        List<TechStackVO> stackList = techStackService.getAllStacks();

        // null 또는 빈 문자열 처리 추가
        List<String> selectedStacks = new ArrayList<>();
        String stacks = jobPostVO.getRequiredSkills();
        if (stacks != null && !stacks.trim().isEmpty()) {
            selectedStacks = Arrays.stream(stacks.split(","))
                    .map(String::trim) // ← 공백 제거!
                    .collect(Collectors.toList());
        }

        ModelAndView modelAndView = new ModelAndView("employer/jobpost/jobpost_edit");
        modelAndView.addObject("jobPostVO", jobPostVO);
        modelAndView.addObject("stackList", stackList);
        modelAndView.addObject("selectedStacks", selectedStacks);

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

}
