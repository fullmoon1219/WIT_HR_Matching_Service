package org.wit.hrmatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.AdminDAO;
import org.wit.hrmatching.dto.admin.AdminDashboardStatsDTO;
import org.wit.hrmatching.vo.JobPostVO;
import org.wit.hrmatching.vo.UserVO;

import java.util.List;

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
}
