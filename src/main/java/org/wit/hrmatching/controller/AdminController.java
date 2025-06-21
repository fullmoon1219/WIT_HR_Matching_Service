package org.wit.hrmatching.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wit.hrmatching.dto.admin.AdminDashboardStatsDTO;
import org.wit.hrmatching.service.AdminService;
import org.wit.hrmatching.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        AdminDashboardStatsDTO statsDTO = adminService.getDashboardStats();

        model.addAttribute("stats", statsDTO);

        model.addAttribute("recentUsers", adminService.getRecentUsers());
        model.addAttribute("recentJobPosts", adminService.getRecentJobPosts());

        return "admin/dashboard";
    }
}
