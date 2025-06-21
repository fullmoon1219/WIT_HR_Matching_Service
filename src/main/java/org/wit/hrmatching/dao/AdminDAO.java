package org.wit.hrmatching.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.dto.admin.AdminDashboardStatsDTO;
import org.wit.hrmatching.mapper.AdminMapper;
import org.wit.hrmatching.vo.JobPostVO;
import org.wit.hrmatching.vo.UserVO;

import java.util.List;

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
}
