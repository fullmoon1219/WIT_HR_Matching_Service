package org.wit.hrmatching.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.dto.admin.AdminDashboardStatsDTO;
import org.wit.hrmatching.vo.JobPostVO;
import org.wit.hrmatching.vo.UserVO;

import java.util.List;

@Mapper
public interface AdminMapper {
    AdminDashboardStatsDTO getDashboardStats();

    List<UserVO> getRecentUsers();

    List<JobPostVO> getRecentJobPosts();
}
