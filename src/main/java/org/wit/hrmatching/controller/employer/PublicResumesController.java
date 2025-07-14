package org.wit.hrmatching.controller.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.wit.hrmatching.service.employer.PublicResumeService;
import org.wit.hrmatching.service.employer.TechStackService;
import org.wit.hrmatching.vo.user.CustomUserDetails;
import org.wit.hrmatching.vo.resume.PublicResumeVO;
import org.wit.hrmatching.vo.job.TechStackVO;

import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@RequestMapping("/employer/publicResume")
public class PublicResumesController {

    private final TechStackService techStackService;
    private final PublicResumeService publicResumeService;

    // (1) 기본 진입 페이지 (기술스택만 전달)
    @GetMapping("/view")
    public ModelAndView viewPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<TechStackVO> stackList = techStackService.getAllStacks();
        ModelAndView model = new ModelAndView("employer/publicResume/view");
        model.addObject("stackList", stackList);
        return model;
    }

    @PostMapping(value = "/view", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> searchAjax(
            @RequestParam(value = "preferred_location", required = false) List<String> locations,
            @RequestParam(value = "skills", required = false) List<Long> skills,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        // 기술스택 전체 조회
        List<TechStackVO> stackList = techStackService.getAllStacks();
        Map<Long, String> stackMap = stackList.stream()
                .collect(Collectors.toMap(TechStackVO::getId, TechStackVO::getName));

        // 검색 조건에 따라 이력서 조회
        List<PublicResumeVO> resultList = publicResumeService.searchApplicants(locations, skills, keyword);
        // 각 resume.skills → skillNames 변환
        for (PublicResumeVO resume : resultList) {
            if (resume.getSkills() != null && !resume.getSkills().isBlank()) {
                List<String> skillNames = Arrays.stream(resume.getSkills().split(","))
                        .map(String::trim)
                        .map(idStr -> {
                            try {
                                Long id = Long.parseLong(idStr);
                                return stackMap.get(id);
                            } catch (NumberFormatException e) {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .toList();
                resume.setSkillNames(skillNames);
            }
        }
        // JSON 응답으로 반환
        Map<String, Object> result = new HashMap<>();
        result.put("stackList", stackList);
        result.put("resumes", resultList);
        result.put("selectedSkillIds", skills); // List<Long> 그대로 전달

        return result;
    }
    // 이력서 상세 조회 (Ajax 요청 처리)
    @GetMapping("/resume_detail")
    public ModelAndView getResumeDetail(@RequestParam("resumeId") Long resumeId) {
        PublicResumeVO resume = publicResumeService.selectResumeById(resumeId);
        System.out.println(resumeId);

        if (resume == null) {
            throw new IllegalArgumentException("해당 이력서를 찾을 수 없습니다: " + resumeId);
        }
        // 기술스택 전체 조회
        List<TechStackVO> stackList = techStackService.getAllStacks();
        Map<Long, String> stackMap = stackList.stream()
                .collect(Collectors.toMap(TechStackVO::getId, TechStackVO::getName));

        if (resume.getSkills() != null && !resume.getSkills().isBlank()) {
            List<String> skillNames = Arrays.stream(resume.getSkills().split(","))
                    .map(String::trim)
                    .map(idStr -> {
                        try {
                            Long id = Long.parseLong(idStr);
                            return stackMap.get(id);
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();
            resume.setSkillNames(skillNames);
        }

        ModelAndView mav = new ModelAndView("employer/publicResume/resume_detail");
        mav.addObject("resume", resume);
        return mav;
    }
}
