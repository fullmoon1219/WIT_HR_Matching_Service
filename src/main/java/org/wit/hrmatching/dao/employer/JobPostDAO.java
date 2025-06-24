package org.wit.hrmatching.dao.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.JobPostMapper;
import org.wit.hrmatching.vo.ApplicantProfilesVO;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JobPostDAO {


    private final JobPostMapper jobPostMapper;

    public int registerJobPost(JobPostVO jobpostVO) {

        int flag = 1;
        int result = jobPostMapper.registerJobPost(jobpostVO);

        if (result == 1) {
            flag = 0;
        }
        return result;
    }

    public List<JobPostVO> selectRecentJobPostList(long userId) {
        return jobPostMapper.selectRecentJobPostList(userId);
    }

    public List<JobPostVO> selectJobPostAllList(long userId) {
        return jobPostMapper.selectJobPostAllList(userId);
    }

    public List<ApplicantProfilesVO> selectApplicantList(long userId) {
        return jobPostMapper.selectApplicantList(userId);
    }

}
