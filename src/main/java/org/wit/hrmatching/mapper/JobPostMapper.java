package org.wit.hrmatching.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.ApplicantProfilesVO;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;
import org.wit.hrmatching.vo.ResumeVO;

import java.util.List;

@Mapper
public interface JobPostMapper {

    int registerJobPost(JobPostVO jobPostVO);
    List<JobPostVO> selectRecentJobPostList(long userId);
    List<JobPostVO> selectJobPostAllList(long userId);
    List<ApplicantProfilesVO> selectApplicantList(long userId);
    JobPostVO selectJobPostById(Long recruitId);
    JobPostVO selectJobPostDetail(Long jobPostId);
    int updateJobPostDetail(JobPostVO jobPostVO);
    int deleteJobPost(long jobPostId);
}
