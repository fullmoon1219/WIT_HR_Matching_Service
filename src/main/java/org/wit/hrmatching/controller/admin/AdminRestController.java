package org.wit.hrmatching.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import org.wit.hrmatching.dto.admin.AdminDashboardStatsDTO;
import org.wit.hrmatching.service.admin.AdminService;
import org.wit.hrmatching.vo.UserVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminRestController {

    private final AdminService adminService;

    /**
     * 대시보드의 총 통계 정보 반환
     */
    @GetMapping("/dashboard/stats")
    public Map<String, Object> getUserStats() {
        Map<String, Object> result = new HashMap<>();
        AdminDashboardStatsDTO stats = adminService.getDashboardStats();

        result.put("userCount", stats.getUserCount());
        result.put("applicantCount", stats.getApplicantCount());
        result.put("companyCount", stats.getCompanyCount());
        result.put("warningCount", stats.getWarningCount());

        result.put("resumeCount", stats.getResumeCount());
        result.put("jobPostCount", stats.getJobPostCount());
        result.put("applicationCount", stats.getApplicationCount());

        result.put("suspendedUserCount", adminService.getSuspendedUserCount());
        result.put("unverifiedEmailUserCount", adminService.getUnverifiedEmailUserCount());

        return result;
    }

    /**
     * 최근 가입한 사용자 목록 반환
     */
    @GetMapping("/dashboard/recent-users")
    public List<UserVO> getRecentUsers() {
        return adminService.getRecentUsers();
    }

    /**
     * 최근 등록된 채용공고 목록 반환
     */
    @GetMapping("/dashboard/recent-job-posts")
    public List<?> getRecentJobPosts() {
        return adminService.getRecentJobPosts();
    }

    /**
     * 최근 5일간 일일 가입자 수 반환
     */
    @GetMapping("/dashboard/daily-user-counts")
    public Map<String, Integer> getDailyUserCounts() {
        return adminService.getDailyUserCountsForPastDays(5);
    }

    /**
     * 최근 5일간 일일 로그인 수 반환
     */
    @GetMapping("/dashboard/daily-login-counts")
    public Map<String, Integer> getDailyLoginCounts() {
        return adminService.getDailyLoginCountsForPastDays(5);
    }

    /**
     * 사용자 역할(APPLICANT, EMPLOYER, ADMIN) 분포 반환
     */
    @GetMapping("/dashboard/user-role-distribution")
    public Map<String, Integer> getUserRoleDistribution() {
        return adminService.getUserRoleDistribution();
    }

    /**
     * 로그인 유형(EMAIL, GOOGLE, NAVER 등) 분포 반환
     */
    @GetMapping("/dashboard/login-type-distribution")
    public Map<String, Integer> getLoginTypeDistribution() {
        return adminService.getLoginTypeDistribution();
    }

    /**
     * 오늘 등록된 이력서 수 반환
     */
    @GetMapping("/dashboard/daily-resume-counts")
    public Map<String, Integer> getDailyResumeCounts() {
        return adminService.getDailyResumeCountsForPastDays(5);
    }

    /**
     * 이력서 작성 완료율 정보 반환
     */
    @GetMapping("/dashboard/resume-completion-stats")
    public Map<String, Object> getResumeCompletionStats() {
        return adminService.getResumeCompletionStats();
    }

    /**
     * 직무별 이력서 분포 반환
     */
    @GetMapping("/dashboard/resume-job-distribution")
    public Map<String, Integer> getResumeJobDistribution() {
        return adminService.getResumeJobDistribution();
    }

    /**
     * 오늘 등록된 채용공고 수 반환
     */
    @GetMapping("/dashboard/daily-jobpost-counts")
    public Map<String, Integer> getDailyJobPostCounts() {
        return adminService.getDailyJobPostCountsForPastDays(5);
    }

    /**
     * 직무별 공고 분포 반환
     */
    @GetMapping("/dashboard/job-post-category-distribution")
    public Map<String, Integer> getJobPostCategoryDistribution() {
        return adminService.getJobPostCategoryDistribution();
    }

    /**
     * 전체 사용자 목록 (프로필 포함) 반환
     */
    @GetMapping("/users")
    public Map<String, Object> getUsers(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserVO> pageResult = adminService.getPagedUsers(pageable);

        Map<String, Object> result = new HashMap<>();
        result.put("content", pageResult.getContent());
        result.put("totalElements", pageResult.getTotalElements());
        result.put("totalPages", pageResult.getTotalPages());
        result.put("size", pageResult.getSize());
        result.put("number", pageResult.getNumber());
        result.put("last", pageResult.isLast());
        result.put("first", pageResult.isFirst());

        return result;
    }
}
