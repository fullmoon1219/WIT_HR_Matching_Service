package org.wit.hrmatching.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wit.hrmatching.dto.admin.AdminDashboardStatsDTO;
import org.wit.hrmatching.vo.JobPostVO;
import org.wit.hrmatching.vo.UserVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {

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

    // 계정 상태
    @Select("SELECT COUNT(*) FROM users WHERE status = 'SUSPENDED'")
    int getSuspendedUserCount();

    @Select("SELECT COUNT(*) FROM users WHERE email_verified = 0")
    int getUnverifiedEmailUserCount();

    List<UserVO> getPagedUsersWithFilter(@Param("limit") int limit,
                                         @Param("offset") int offset,
                                         @Param("role") String role,
                                         @Param("status") String status,
                                         @Param("warning") String warning,
                                         @Param("verified") String verified,
                                         @Param("keyword") String keyword);


    int countUsersWithFilter(@Param("role") String role,
                             @Param("status") String status,
                             @Param("warning") String warning,
                             @Param("verified") String verified,
                             @Param("keyword") String keyword);
}
