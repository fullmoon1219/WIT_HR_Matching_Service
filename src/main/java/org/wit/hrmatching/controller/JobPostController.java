package org.wit.hrmatching.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.wit.hrmatching.service.JobPostService;
import org.wit.hrmatching.vo.JobPostVO;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class JobPostController {

    private final JobPostService jobPostService;

    @RequestMapping("/jobpost") //------------- 채용공고 작성
    public ModelAndView registerJobPost() {

        ModelAndView modelAndView = new ModelAndView("jobpost");

        return modelAndView;
    }

    @RequestMapping("/jobpost_ok") //------------- 채용공고 작성완료
    public String jobpost_ok(HttpServletRequest request, Model model) {
        JobPostVO to = new JobPostVO();

        to.setTitle(request.getParameter("title"));
        to.setDescription(request.getParameter("description"));
        to.setRequiredSkills(request.getParameter("requiredSkills"));

        String salaryParam = request.getParameter("salary");
        if (salaryParam != null && !salaryParam.isEmpty()) {
            try {
                to.setSalary(Integer.parseInt(salaryParam));
            } catch (NumberFormatException e) {
                to.setSalary(0);
            }
        }

        to.setLocation(request.getParameter("location"));

        String employmentTypeParam = request.getParameter("employmentType");
        try {
            to.setEmploymentType(JobPostVO.EmploymentType.valueOf(employmentTypeParam.toUpperCase()));
        } catch (IllegalArgumentException | NullPointerException e) {
            to.setEmploymentType(JobPostVO.EmploymentType.FULLTIME);
        }

        String deadlineParam = request.getParameter("deadline");
        if (deadlineParam != null && !deadlineParam.isEmpty()) {
            try {
                to.setDeadline(LocalDate.parse(deadlineParam));
            } catch (Exception e) {
                to.setDeadline(null);
            }
        }

        to.setTags(request.getParameter("tags"));

        int flag = jobPostService.registerJobPost(to);
        model.addAttribute("flag", flag);

        return "jobpost_ok";
    }
}
