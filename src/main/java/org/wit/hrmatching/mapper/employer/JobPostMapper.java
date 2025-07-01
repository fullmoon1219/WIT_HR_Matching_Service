package org.wit.hrmatching.mapper.employer;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.ApplicantProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;

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
