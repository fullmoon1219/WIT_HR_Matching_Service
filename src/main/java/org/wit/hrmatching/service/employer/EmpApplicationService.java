package org.wit.hrmatching.service.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.employer.EmpApplicationDAO;
import org.wit.hrmatching.vo.application.EmpApplicationVO;
import org.wit.hrmatching.vo.application.EmployerRecentApplicantVO;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmpApplicationService {

    private final EmpApplicationDAO empApplicationDAO;

    public List<EmployerRecentApplicantVO> selectApplicantToEmployerList(Long userId, String keyword, int offset, int size) {
        return empApplicationDAO.selectApplicantToEmployerList(userId, keyword, offset, size);
    }

    public int countApplicantList(Long userId, String keyword) {
        return empApplicationDAO.countApplicantList(userId, keyword);
    }


    public EmpApplicationVO selectResumeDetail(Long resumeId, Long jobPostId) {
        return empApplicationDAO.selectResumeDetail(resumeId,jobPostId);
    }

    public int updateViewAt(Long applicationId) {
        return empApplicationDAO.updateViewAt(applicationId);
    }

    public int updateStatus(Long applicationId, String status) {
        return empApplicationDAO.updateStatus(applicationId,status);
    }

    public List<Map<String, Object>> countApplicationsByStatus(Long userId) {
        return empApplicationDAO.countApplicationsByStatus(userId);
    }

    public Long countUnviewedApplications(Long userId) {
        return empApplicationDAO.countUnviewedApplications(userId);
    }
}
