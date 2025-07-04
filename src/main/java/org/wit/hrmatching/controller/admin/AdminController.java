package org.wit.hrmatching.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String userManagementPage() {
        return "admin/user-management";
    }

    @GetMapping("/resumes")
    public String resumeLists() {
        return "admin/resume-list";
    }

    @GetMapping("/jobPosts")
    public String postLists() {
        return "admin/jobPosts";
    }
}
