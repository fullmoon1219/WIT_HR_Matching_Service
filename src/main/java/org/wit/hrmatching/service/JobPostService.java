package org.wit.hrmatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.employer.JobPostDAO;
import org.wit.hrmatching.vo.ApplicantProfilesVO;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;
import org.wit.hrmatching.vo.ResumeVO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostService {

    private final JobPostDAO jobPostDAO;

    public int registerJobPost(JobPostVO jobPostVO) {
        return jobPostDAO.registerJobPost(jobPostVO);
    }

    public List<JobPostVO> selectRecentJobPostList(long userId) {
        return jobPostDAO.selectRecentJobPostList(userId);
    }

    public List<JobPostVO> selectJobPostAllList(long userId) {
        return jobPostDAO.selectJobPostAllList(userId);
    }

    public List<ApplicantProfilesVO> selectApplicantList(long userId) {
        return jobPostDAO.selectApplicantList(userId);
    }
}
