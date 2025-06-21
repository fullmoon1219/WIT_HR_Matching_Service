package org.wit.hrmatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.AdminDAO;
import org.wit.hrmatching.dto.admin.AdminDashboardStatsDTO;
import org.wit.hrmatching.vo.JobPostVO;
import org.wit.hrmatching.vo.UserVO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminDAO adminDAO;

    public AdminDashboardStatsDTO getDashboardStats() {
        return adminDAO.getDashboardStats();
    }

    public List<UserVO> getRecentUsers() {
        return adminDAO.getRecentUsers();
    }

    public List<JobPostVO> getRecentJobPosts() {
        return adminDAO.getRecentJobPosts();
    }

    public Map<String, Integer> getDailyUserCountsForPastDays(int days) {
        Map<String, Integer> result = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            String label = date.format(formatter);
            int count = adminDAO.countUsersByDate(date);
            result.put(label, count);
        }

        return result;
    }

    public List<Map<String, Object>> getDailyUserCount() {
        return adminDAO.getDailyUserCount();
    }

    public List<Map<String, Object>> getDailyLoginCount() {
        return adminDAO.getDailyLoginCount();
    }

    public List<Map<String, Object>> getUserRoleDistribution() {
        return adminDAO.getUserRoleDistribution();
    }

    public List<Map<String, Object>> getLoginTypeDistribution() {
        return adminDAO.getLoginTypeDistribution();
    }

    public List<Map<String, Object>> getDailyResumeCount() {
        return adminDAO.getDailyResumeCount();
    }

    public Map<String, Object> getResumeCompletionStats() {
        return adminDAO.getResumeCompletionStats();
    }

    public List<Map<String, Object>> getResumeJobDistribution() {
        return adminDAO.getResumeJobDistribution();
    }

    public List<Map<String, Object>> getDailyJobPostCount() {
        return adminDAO.getDailyJobPostCount();
    }

    public List<Map<String, Object>> getJobPostCategoryDistribution() {
        return adminDAO.getJobPostCategoryDistribution();
    }

    public int getClosingSoonJobPostCount() {
        return adminDAO.getClosingSoonJobPostCount();
    }

    public int getSuspendedUserCount() {
        return adminDAO.getSuspendedUserCount();
    }

    public int getUnverifiedEmailUserCount() {
        return adminDAO.getUnverifiedEmailUserCount();
    }

    // 전체 사용자
    public List<UserVO> getAllUsersWithProfiles() {
        return adminDAO.getAllUsersWithProfiles();
    }
}
