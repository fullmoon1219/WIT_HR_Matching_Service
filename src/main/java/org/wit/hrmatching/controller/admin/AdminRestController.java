package org.wit.hrmatching.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.dto.admin.AdminDashboardStatsDTO;
import org.wit.hrmatching.dto.admin.PagedResponseDTO;
import org.wit.hrmatching.service.admin.AdminService;
import org.wit.hrmatching.vo.user.UserVO;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminRestController {

    private final AdminService adminService;

    // 1. 전체 사용자 목록 조회
    @GetMapping("/users")
    public PagedResponseDTO<UserVO> getUsers(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(required = false) String role,
                                             @RequestParam(required = false) String status,
                                             @RequestParam(required = false) String warning,
                                             @RequestParam(required = false) String verified,
                                             @RequestParam(required = false) String keyword) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<UserVO> pageResult = adminService.getPagedUsers(pageable, null, role, status, warning, verified, keyword);

        return PagedResponseDTO.<UserVO>builder()
                .content(pageResult.getContent())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .size(pageResult.getSize())
                .number(pageResult.getNumber() + 1)
                .first(pageResult.isFirst())
                .last(pageResult.isLast())
                .numberOfElements(pageResult.getNumberOfElements())
                .empty(pageResult.isEmpty())
                .build();
    }

    // 2. 단일 사용자 조회
    @GetMapping("/users/{id}")
    public ResponseEntity<UserVO> getUserById(@PathVariable("id") Integer id) {
        UserVO user = adminService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // 3. 최근 가입자
    @GetMapping("/dashboard/recent-users")
    public List<UserVO> getRecentUsers() {
        return adminService.getRecentUsers();
    }

    // 4. 최근 채용공고
    @GetMapping("/dashboard/recent-job-posts")
    public List<?> getRecentJobPosts() {
        return adminService.getRecentJobPosts();
    }

    // 5. 대시보드 통계
    @GetMapping("/dashboard/stats")
    public AdminDashboardStatsDTO getUserStats() {
        return adminService.getDashboardStats();
    }

    // 6. 일일 가입자 수
    @GetMapping("/dashboard/daily-user-counts")
    public Map<String, Integer> getDailyUserCounts() {
        return adminService.getDailyUserCountsForPastDays(5);
    }

    // 7. 일일 로그인 수
    @GetMapping("/dashboard/daily-login-counts")
    public Map<String, Integer> getDailyLoginCounts() {
        return adminService.getDailyLoginCountsForPastDays(5);
    }

    // 8. 사용자 역할 분포
    @GetMapping("/dashboard/user-role-distribution")
    public Map<String, Integer> getUserRoleDistribution() {
        return adminService.getUserRoleDistribution();
    }

    // 9. 로그인 유형 분포
    @GetMapping("/dashboard/login-type-distribution")
    public Map<String, Integer> getLoginTypeDistribution() {
        return adminService.getLoginTypeDistribution();
    }

    // 10. 일일 이력서 수
    @GetMapping("/dashboard/daily-resume-counts")
    public Map<String, Integer> getDailyResumeCounts() {
        return adminService.getDailyResumeCountsForPastDays(5);
    }

    // 11. 이력서 작성 완료율
    @GetMapping("/dashboard/resume-completion-stats")
    public Map<String, Object> getResumeCompletionStats() {
        return adminService.getResumeCompletionStats();
    }

    // 12. 직무별 이력서 분포
    @GetMapping("/dashboard/resume-job-distribution")
    public Map<String, Integer> getResumeJobDistribution() {
        return adminService.getResumeJobDistribution();
    }

    // 13. 일일 채용공고 수
    @GetMapping("/dashboard/daily-jobpost-counts")
    public Map<String, Integer> getDailyJobPostCounts() {
        return adminService.getDailyJobPostCountsForPastDays(5);
    }

    // 14. 직무별 공고 분포
    @GetMapping("/dashboard/job-post-category-distribution")
    public Map<String, Integer> getJobPostCategoryDistribution() {
        return adminService.getJobPostCategoryDistribution();
    }
}