package org.wit.hrmatching.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wit.hrmatching.dto.admin.AdminDashboardStatsDTO;
import org.wit.hrmatching.service.AdminService;
import org.wit.hrmatching.vo.UserVO;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        AdminDashboardStatsDTO statsDTO = adminService.getDashboardStats();

        model.addAttribute("stats", statsDTO);

        model.addAttribute("recentUsers", adminService.getRecentUsers());
        model.addAttribute("recentJobPosts", adminService.getRecentJobPosts());

        Map<String, Integer> dailyUserCounts = adminService.getDailyUserCountsForPastDays(5);

        model.addAttribute("dailyUserCounts", dailyUserCounts);
        model.addAttribute("dailyUserCount", adminService.getDailyUserCount());
        model.addAttribute("dailyLoginCount", adminService.getDailyLoginCount());
        model.addAttribute("userRoleDistribution", adminService.getUserRoleDistribution());
        model.addAttribute("loginTypeDistribution", adminService.getLoginTypeDistribution());
        model.addAttribute("dailyResumeCount", adminService.getDailyResumeCount());
        model.addAttribute("resumeCompletionStats", adminService.getResumeCompletionStats());
        model.addAttribute("resumeJobDistribution", adminService.getResumeJobDistribution());
        model.addAttribute("dailyJobPostCount", adminService.getDailyJobPostCount());
        model.addAttribute("jobPostCategoryDistribution", adminService.getJobPostCategoryDistribution());
//        model.addAttribute("closingSoonJobPostCount", adminService.getClosingSoonJobPostCount());

        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        AdminDashboardStatsDTO statsDTO = adminService.getDashboardStats();

        model.addAttribute("stats", statsDTO);
        model.addAttribute("suspendedUserCount", adminService.getSuspendedUserCount());
        model.addAttribute("unverifiedEmailUserCount", adminService.getUnverifiedEmailUserCount());

        List<UserVO> userList = adminService.getAllUsersWithProfiles();
        model.addAttribute("userList", userList);

        return "admin/user-management";
    }
}
