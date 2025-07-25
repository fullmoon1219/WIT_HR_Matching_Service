package org.wit.hrmatching.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dto.admin.AdminDashboardStatsDTO;
import org.wit.hrmatching.mapper.admin.AdminMapper;
import org.wit.hrmatching.mapper.admin.AdminMemoMapper;
import org.wit.hrmatching.vo.admin.AdminMemoVO;
import org.wit.hrmatching.vo.job.JobPostVO;
import org.wit.hrmatching.vo.user.UserVO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminMapper adminMapper;
    private final AdminMemoMapper adminMemoMapper;

    private Map<String, Object> getDateRangeParams(int days) {
        Map<String, Object> params = new LinkedHashMap<>();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        params.put("startDate", startDate.atStartOfDay()); // inclusive
        params.put("endDate", endDate.plusDays(1).atStartOfDay()); // exclusive
        return params;
    }

    public UserVO getUserById(Integer id) {
        return adminMapper.selectUserById(id);
    }

    public AdminDashboardStatsDTO getDashboardStats() {
        return adminMapper.getDashboardStats();
    }

    public List<UserVO> getRecentUsers() {
        return adminMapper.getRecentUsers();
    }

    public List<JobPostVO> getRecentJobPosts() {
        return adminMapper.getRecentJobPosts();
    }

    public Map<String, Integer> getDailyUserCountsForPastDays(int days) {
        return getDailyCountsForPastDays(days, "users", "create_at");
    }

    public Map<String, Integer> getDailyLoginCountsForPastDays(int days) {
        return getDailyCountsForPastDays(days, "login_history", "login_at");
    }

    public Map<String, Integer> getDailyResumeCountsForPastDays(int days) {
        return getDailyCountsForPastDays(days, "resumes", "create_at");
    }

    public Map<String, Integer> getDailyJobPostCountsForPastDays(int days) {
        return getDailyCountsForPastDays(days, "job_posts", "create_at");
    }

    private Map<String, Integer> getDailyCountsForPastDays(int days, String table, String column) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        List<Map<String, Object>> rawData = adminMapper.getDailyCount(table, column, startDate, endDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        Map<String, Integer> result = new LinkedHashMap<>();

        // 초기값 0으로 세팅
        for (int i = 0; i < days; i++) {
            LocalDate date = startDate.plusDays(i);
            result.put(date.format(formatter), 0);
        }

        for (Map<String, Object> row : rawData) {
            LocalDate date = ((java.sql.Date) row.get("stat_date")).toLocalDate();
            int count = ((Number) row.get("count")).intValue();
            result.put(date.format(formatter), count);
        }

        return result;
    }


    public Map<String, Integer> getUserRoleDistribution() {
        List<Map<String, Object>> list = adminMapper.getUserRoleDistribution();
        Map<String, Integer> map = new LinkedHashMap<>();
        for (Map<String, Object> row : list) {
            map.put((String) row.get("role"), ((Number) row.get("count")).intValue());
        }
        return map;
    }

    public Map<String, Integer> getLoginTypeDistribution() {
        List<Map<String, Object>> list = adminMapper.getLoginTypeDistribution();
        Map<String, Integer> map = new LinkedHashMap<>();
        for (Map<String, Object> row : list) {
            map.put((String) row.get("login_type"), ((Number) row.get("count")).intValue());
        }
        return map;
    }

    public Map<String, Object> getResumeCompletionStats() {
        return adminMapper.getResumeCompletionStats();
    }

    public Map<String, Integer> getResumeJobDistribution() {
        List<Map<String, Object>> list = adminMapper.getResumeJobDistribution();
        Map<String, Integer> map = new LinkedHashMap<>();
        for (Map<String, Object> row : list) {
            map.put((String) row.get("job_category"), ((Number) row.get("count")).intValue());
        }
        return map;
    }

    public Map<String, Integer> getJobPostCategoryDistribution() {
        List<Map<String, Object>> list = adminMapper.getJobPostCategoryDistribution();
        Map<String, Integer> map = new LinkedHashMap<>();
        for (Map<String, Object> row : list) {
            map.put((String) row.get("category"), ((Number) row.get("count")).intValue());
        }
        return map;
    }

    public Page<UserVO> getPagedUsers(Pageable pageable,
                                      Integer userId,
                                      String role,
                                      String status,
                                      String warning,
                                      String verified,
                                      Boolean deleted,
                                      String keyword) {

        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();

        int total = adminMapper.countUsersWithFilter(userId, role, status, warning, verified, deleted, keyword);
        List<UserVO> users = adminMapper.getPagedUsersWithFilter(limit, offset, userId, role, status, warning, verified, deleted, keyword);

        return new PageImpl<>(users, pageable, total);
    }

    public void addMemo(AdminMemoVO memo) {
        adminMemoMapper.insertMemo(memo);
    }

    public void removeMemo(Long id) {
        adminMemoMapper.deleteMemo(id);
    }

    public List<AdminMemoVO> listMemos() {
        return adminMemoMapper.getAllMemos();
    }

    public List<Map<String, Object>> getAccountStatusRatio() {
        return adminMapper.getAccountStatusRatio();
    }

    public List<Map<String, Object>> getWarningDistribution() {
        return adminMapper.getWarningDistribution();
    }


}
