package org.wit.hrmatching.dao.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.employer.EmpApplicationMapper;
import org.wit.hrmatching.vo.EmpApplicationVO;
import org.wit.hrmatching.vo.EmployerRecentApplicantVO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class EmpApplicationDAO {

    private final EmpApplicationMapper empApplicationMapper;

    public List<EmployerRecentApplicantVO> selectApplicantToEmployerList(Long userId, String keyword, int offset, int size) {
        return empApplicationMapper.selectApplicantToEmployerList(userId, keyword, offset, size);
    }

    public int countApplicantList(Long userId, String keyword) {
        return empApplicationMapper.countApplicantList(userId, keyword);
    }


    public EmpApplicationVO selectResumeDetail(Long resumeId, Long jobPostId) {
        return empApplicationMapper.selectResumeDetail(resumeId,jobPostId);
    }
    public int updateViewAt(Long applicationId) {
        return empApplicationMapper.updateViewAt(applicationId);
    }

    public int updateStatus(Long applicationId, String status) {
        return empApplicationMapper.updateStatus(applicationId,status);
    }

    public List<Map<String, Object>> countApplicationsByStatus(Long userId) {
        return empApplicationMapper.countApplicationsByStatus(userId);
    }

    public Long countUnviewedApplications(Long userId) {
        return empApplicationMapper.countUnviewedApplications(userId);
    }
}
