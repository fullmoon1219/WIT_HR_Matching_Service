package org.wit.hrmatching.controller.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.wit.hrmatching.service.employer.EmpApplicationService;
import org.wit.hrmatching.service.employer.JobPostService;
import org.wit.hrmatching.service.employer.EmployerProfileService;
import org.wit.hrmatching.service.employer.TechStackService;
import org.wit.hrmatching.vo.application.EmpApplicationVO;
import org.wit.hrmatching.vo.application.EmployerRecentApplicantVO;
import org.wit.hrmatching.vo.job.JobPostVO;
import org.wit.hrmatching.vo.user.CustomUserDetails;
import org.wit.hrmatching.vo.user.EmployerProfilesVO;

import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@RequestMapping("/employer/application")
public class EmpApplicationController {
    private final EmpApplicationService empApplicationService;

    private final JobPostService jobPostService;
    private final EmployerProfileService employerProfileService;
    private final TechStackService techStackService;

    @GetMapping("/list")
    public ModelAndView postApplicant(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        Long userId = userDetails.getUser().getId();
        int pageSize = 10;
        int offset = (page - 1) * pageSize;

        // ✅ 검색 + 페이징 결과 가져오기
        List<EmployerRecentApplicantVO> vo = empApplicationService
                .selectApplicantToEmployerList(userId, keyword, offset, pageSize);

        // ✅ 전체 개수 (검색 포함)
        int totalCount = empApplicationService.countApplicantList(userId, keyword);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // ✅ 상태별 카운트 (기존 유지)
        List<Map<String, Object>> cntMap = empApplicationService.countApplicationsByStatus(userId);
        Map<String, Long> statusCountMap = new HashMap<>();
        for (Map<String, Object> row : cntMap) {
            String status = (String) row.get("status");
            Long count = ((Number) row.get("count")).longValue();
            statusCountMap.put(status, count);
        }
        Long cnt = empApplicationService.countUnviewedApplications(userId);
        statusCountMap.put("UNREAD_CNT", cnt);

        ModelAndView modelAndView = new ModelAndView("employer/empApplication/list");
        modelAndView.addObject("ApplicantList", vo);
        modelAndView.addObject("Cnt", statusCountMap);
        modelAndView.addObject("keyword", keyword); // 검색 유지
        modelAndView.addObject("currentPage", page); // 현재 페이지
        modelAndView.addObject("totalPages", totalPages); // 총 페이지

        return modelAndView;
    }

    @GetMapping("/resume_detail")
    public ModelAndView  postApplicantDetail(@RequestParam("resumeId") Long resumeId
                                            , @RequestParam("jobPostId") Long jobPostId) {
        EmpApplicationVO empApplicationVO = empApplicationService.selectResumeDetail(resumeId,jobPostId);
        if(empApplicationVO.getViewedAt()== null) {
            empApplicationService.updateViewAt(empApplicationVO.getApplicationId());
            empApplicationVO = empApplicationService.selectResumeDetail(resumeId, jobPostId);
        }
        ModelAndView modelAndView = new ModelAndView("employer/empApplication/resume_detail"); // 💡 실제 템플릿 경로 맞게 수정
        modelAndView.addObject("resume", empApplicationVO); // Thymeleaf에서 ${jobPost.xxx}로 접근 가능

        return modelAndView;
    }

    @GetMapping("/jobpost_detail")
    public ModelAndView showJobPostDetail(@RequestParam("postId") Long postId) {
        JobPostVO jobPostVO = jobPostService.selectJobPostDetail(postId);
        EmployerProfilesVO eVO = employerProfileService.getEmployerProfile(jobPostVO.getUserId());

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

        ModelAndView modelAndView = new ModelAndView("employer/empApplication/jobpost_detail"); // 💡 실제 템플릿 경로 맞게 수정
        modelAndView.addObject("jobPost", jobPostVO); // Thymeleaf에서 ${jobPost.xxx}로 접근 가능
        modelAndView.addObject("employerProfile", eVO);
        modelAndView.addObject("selectedStacks", selectedStackNames);
        return modelAndView;
    }

    @PostMapping("/update_status")
    @ResponseBody
    public ResponseEntity<?> updateApplicationStatus(@RequestBody Map<String, Object> request) {
        Long applicationId = ((Number) request.get("applicationId")).longValue();
        String status = (String) request.get("status");

        int flag = empApplicationService.updateStatus(applicationId, status);
        if (flag > 0) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("상태 변경 실패");
        }
    }

}
