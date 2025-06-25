package org.wit.hrmatching.dao.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.JobPostMapper;
import org.wit.hrmatching.vo.ApplicantProfilesVO;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;
import org.wit.hrmatching.vo.ResumeVO;

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

    public JobPostVO selectJobPostDetail(long jobPostId) {
        return jobPostMapper.selectJobPostDetail(jobPostId);
    }

    public int editJobPostDetail(JobPostVO jobPostVO) {

        // 수정 성공 시 flag = 0, 실패 시 flag = 1
        int flag = 1;
        int result = jobPostMapper.updateJobPostDetail(jobPostVO);

        if (result == 1) {
            flag = 0;
        }

        return flag;
    }

    public int deleteJobPost(long jobPostId) {

        // 수정 성공 시 flag = 0, 실패 시 flag = 1
        int flag = 1;
        int result = jobPostMapper.deleteJobPost(jobPostId);

        if (result == 1) {
            flag = 0;
        }

        return flag;
    }
}
