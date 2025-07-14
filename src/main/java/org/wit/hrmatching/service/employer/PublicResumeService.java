package org.wit.hrmatching.service.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.employer.PublicResumeDAO;
import org.wit.hrmatching.vo.resume.PublicResumeVO;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicResumeService {

	private final PublicResumeDAO publicResumeDAO;

    public List<PublicResumeVO> searchApplicants(List<String> locations, List<Long> skills, String keyword) {
		return publicResumeDAO.searchApplicants(locations,skills,keyword);
    }

    public PublicResumeVO selectResumeById(Long resumeId) {
        return publicResumeDAO.selectResumeById(resumeId);
    }
}
