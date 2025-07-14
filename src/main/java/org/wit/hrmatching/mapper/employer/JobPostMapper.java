package org.wit.hrmatching.mapper.employer;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.wit.hrmatching.vo.user.ApplicantProfilesVO;
import org.wit.hrmatching.vo.job.JobPostVO;

import java.util.List;

@Mapper
public interface JobPostMapper {

    int insertJobPost (JobPostVO jobPostVO);
    List<JobPostVO> selectRecentJobPostList(long userId);
    List<JobPostVO> selectJobPostAllList(long userId);
    List<ApplicantProfilesVO> selectApplicantList(long userId);
    JobPostVO selectJobPostById(Long recruitId);
    JobPostVO selectJobPostDetail(Long jobPostId);
    int updateJobPostDetail(JobPostVO jobPostVO);

    // 공고 삭제하기
    void softDeleteJobPosts(@Param("ids") List<Long> ids);

    List<JobPostVO> findJobPostsByTitle(@Param("userId") Long userId,
                                        @Param("keyword") String keyword,
                                        @Param("offset") int offset,
                                        @Param("size") int size);

    int countJobPosts(@Param("userId") Long userId,
                      @Param("keyword") String keyword);

    JobPostVO countJobPostStatus(@Param("userId") Long userId);


}
