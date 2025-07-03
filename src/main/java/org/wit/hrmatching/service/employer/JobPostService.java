package org.wit.hrmatching.service.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.employer.JobPostDAO;
import org.wit.hrmatching.vo.ApplicantProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostService {

    private final JobPostDAO jobPostDAO;

    public int insertJobPost(JobPostVO jobPostVO) {
        return jobPostDAO.insertJobPost(jobPostVO);
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

    public JobPostVO selectJobPostDetail(long jobPostId) {
        return jobPostDAO.selectJobPostDetail(jobPostId);
    }

    public boolean editJobPostDetail(JobPostVO jobPostVO) {
        return jobPostDAO.editJobPostDetail(jobPostVO) == 0;
    }

    // 공고 삭제하기
    public void softDeleteJobPosts(List<Long> ids) {
        jobPostDAO.softDeleteJobPosts(ids);
    }

    public List<JobPostVO> getJobPostList(Long userId, String keyword, int offset, int size) {
        return jobPostDAO.findJobPosts(userId, keyword, offset, size);
    }

    public int getJobPostCount(Long userId, String keyword) {
        return jobPostDAO.countJobPosts(userId, keyword);
    }

    public JobPostVO getJobPostStatusCounts(Long userId) {
        return jobPostDAO.countJobPostStatus(userId);
    }

}
