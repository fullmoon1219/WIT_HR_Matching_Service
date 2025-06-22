package org.wit.hrmatching.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.dto.admin.AdminDashboardStatsDTO;
import org.wit.hrmatching.mapper.AdminMapper;
import org.wit.hrmatching.vo.JobPostVO;
import org.wit.hrmatching.vo.UserVO;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

    // 일일 통계 공통
    public List<Map<String, Object>> getDailyCount(String table, String column, LocalDate start, LocalDate end) {
        return adminMapper.getDailyCount(table, column, start, end);
    }

    public List<Map<String, Object>> getUserRoleDistribution() {
        return adminMapper.getUserRoleDistribution();
    }

    public List<Map<String, Object>> getLoginTypeDistribution() {
        return adminMapper.getLoginTypeDistribution();
    }

    public Map<String, Object> getResumeCompletionStats() {
        return adminMapper.getResumeCompletionStats();
    }

    public List<Map<String, Object>> getResumeJobDistribution() {
        return adminMapper.getResumeJobDistribution();
    }

    public List<Map<String, Object>> getJobPostCategoryDistribution() {
        return adminMapper.getJobPostCategoryDistribution();
    }

    public int getSuspendedUserCount() {
        return adminMapper.getSuspendedUserCount();
    }

    public int getUnverifiedEmailUserCount() {
        return adminMapper.getUnverifiedEmailUserCount();
    }

    // 전체 사용자 (페이지 구분)
    public List<UserVO> getAllUsersWithProfilesPaged(int limit, int offset) {
        return adminMapper.getAllUsersWithProfilesPaged(limit, offset);
    }

    // 전체 사용자 수
    public int countAllUsers() {
        return adminMapper.countAllUsers();
    }
}
