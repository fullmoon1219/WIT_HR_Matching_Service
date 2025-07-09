package org.wit.hrmatching.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("pageTitle", "관리자 대시보드");
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String userManagementPage(Model model) {
        model.addAttribute("pageTitle", "사용자 관리");
        return "admin/user-management";
    }

    @GetMapping("/resumes")
    public String resumeLists(Model model) {
        model.addAttribute("pageTitle", "이력서 관리");
        return "admin/resume-list";
    }

    @GetMapping("/jobPosts")
    public String postLists(Model model) {
        model.addAttribute("pageTitle", "공고 관리");
        return "admin/jobPosts";
    }

    @GetMapping("/inquirys")
    public String inquiryPage(Model model) {
        model.addAttribute("pageTitle", "문의 내역 관리");
        return "admin/inquiry-management";
    }

}
