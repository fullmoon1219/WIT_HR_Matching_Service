package org.wit.hrmatching.dao.employer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.employer.PublicResumeMapper;
import org.wit.hrmatching.vo.resume.PublicResumeVO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PublicResumeDAO {

	private final PublicResumeMapper publicResumeMapper;

	public List<PublicResumeVO> searchApplicants(List<String> locations, List<Long> skills, String keyword) {
		return publicResumeMapper.searchApplicants(locations,skills,keyword);
	}

	public PublicResumeVO selectResumeById(Long resumeId) {
		return publicResumeMapper.selectResumeById(resumeId);
	}
}
