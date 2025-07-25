package org.wit.hrmatching.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.wit.hrmatching.dto.admin.AdminDashboardStatsDTO;
import org.wit.hrmatching.vo.job.JobPostVO;
import org.wit.hrmatching.vo.user.UserVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {

    UserVO selectUserById(@Param("id") Integer id);

    // 대시보드 총 통계
    AdminDashboardStatsDTO getDashboardStats();

    // 최근 가입자 / 최근 공고
    List<UserVO> getRecentUsers();
    List<JobPostVO> getRecentJobPosts();

    // 일일 통계 공통 메서드
    List<Map<String, Object>> getDailyCount(@Param("table") String table,
                                            @Param("column") String column,
                                            @Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);


    // 3. 회원 유형 비율
    List<Map<String, Object>> getUserRoleDistribution();

    // 4. 소셜 로그인 비율
    List<Map<String, Object>> getLoginTypeDistribution();

    // 6. 이력서 작성 완료율
    Map<String, Object> getResumeCompletionStats();

    // 7. 직무별 이력서 분포
    List<Map<String, Object>> getResumeJobDistribution();

    // 9. 직무별 공고 분포
    List<Map<String, Object>> getJobPostCategoryDistribution();

    List<Map<String, Object>> getAccountStatusRatio();
    List<Map<String, Object>> getWarningDistribution();

    List<UserVO> getPagedUsersWithFilter(@Param("limit") int limit,
                                         @Param("offset") int offset,
                                         @Param("userId") Integer userId,
                                         @Param("role") String role,
                                         @Param("status") String status,
                                         @Param("warning") String warning,
                                         @Param("verified") String verified,
                                         @Param("deleted") Boolean deleted,
                                         @Param("keyword") String keyword);


    int countUsersWithFilter(@Param("userId") Integer userId,
                             @Param("role") String role,
                             @Param("status") String status,
                             @Param("warning") String warning,
                             @Param("verified") String verified,
                             @Param("deleted") Boolean deleted,
                             @Param("keyword") String keyword);
}
