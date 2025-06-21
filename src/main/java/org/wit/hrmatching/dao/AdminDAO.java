package org.wit.hrmatching.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.dto.admin.AdminDashboardStatsDTO;
import org.wit.hrmatching.mapper.AdminMapper;
import org.wit.hrmatching.vo.JobPostVO;
import org.wit.hrmatching.vo.UserVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AdminDAO {

    private final AdminMapper adminMapper;

    public AdminDashboardStatsDTO getDashboardStats() {
        return adminMapper.getDashboardStats();
    }

    public List<UserVO> getRecentUsers() {
        return adminMapper.getRecentUsers();
    }

    public List<JobPostVO> getRecentJobPosts() {
        return adminMapper.getRecentJobPosts();
    }

    public int countUsersByDate(LocalDate date) {
        return adminMapper.countUsersByDate(date);
    }

    public List<Map<String, Object>> getDailyUserCount() {
        return adminMapper.getDailyUserCount();
    }

    public List<Map<String, Object>> getDailyLoginCount() {
        return adminMapper.getDailyLoginCount();
    }

    public List<Map<String, Object>> getUserRoleDistribution() {
        return adminMapper.getUserRoleDistribution();
    }

    public List<Map<String, Object>> getLoginTypeDistribution() {
        return adminMapper.getLoginTypeDistribution();
    }

    public List<Map<String, Object>> getDailyResumeCount() {
        return adminMapper.getDailyResumeCount();
    }

    public Map<String, Object> getResumeCompletionStats() {
        return adminMapper.getResumeCompletionStats();
    }

    public List<Map<String, Object>> getResumeJobDistribution() {
        return adminMapper.getResumeJobDistribution();
    }

    public List<Map<String, Object>> getDailyJobPostCount() {
        return adminMapper.getDailyJobPostCount();
    }

    public List<Map<String, Object>> getJobPostCategoryDistribution() {
        return adminMapper.getJobPostCategoryDistribution();
    }

    public int getClosingSoonJobPostCount() {
        return adminMapper.getClosingSoonJobPostCount();
    }

    public int getSuspendedUserCount() {
        return adminMapper.getSuspendedUserCount();
    }

    public int getUnverifiedEmailUserCount() {
        return adminMapper.getUnverifiedEmailUserCount();
    }

    // 전체 사용자
    public List<UserVO> getAllUsersWithProfiles() {
        return adminMapper.getAllUsersWithProfiles();
    }
}
