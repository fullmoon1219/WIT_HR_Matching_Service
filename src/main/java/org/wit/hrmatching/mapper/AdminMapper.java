package org.wit.hrmatching.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.wit.hrmatching.dto.admin.AdminDashboardStatsDTO;
import org.wit.hrmatching.vo.JobPostVO;
import org.wit.hrmatching.vo.UserVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {
    AdminDashboardStatsDTO getDashboardStats();

    List<UserVO> getRecentUsers();

    List<JobPostVO> getRecentJobPosts();

    @Select("SELECT COUNT(*) FROM users WHERE DATE(create_at) = #{date}")
    int countUsersByDate(@Param("date") LocalDate date);

    List<Map<String, Object>> getDailyUserCount();
    List<Map<String, Object>> getDailyLoginCount();
    List<Map<String, Object>> getUserRoleDistribution();
    List<Map<String, Object>> getLoginTypeDistribution();
    List<Map<String, Object>> getDailyResumeCount();
    Map<String, Object> getResumeCompletionStats();
    List<Map<String, Object>> getResumeJobDistribution();
    List<Map<String, Object>> getDailyJobPostCount();
    List<Map<String, Object>> getJobPostCategoryDistribution();
    int getClosingSoonJobPostCount();

    @Select("SELECT COUNT(*) FROM users WHERE status = 'SUSPENDED'")
    int getSuspendedUserCount();

    @Select("SELECT COUNT(*) FROM users WHERE email_verified = 0")
    int getUnverifiedEmailUserCount();

    List<UserVO> getAllUsersWithProfiles(); // 전체 사용자

}
