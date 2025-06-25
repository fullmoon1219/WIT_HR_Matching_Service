package org.wit.hrmatching.service.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.applicant.RecruitDAO;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;

@Service
@RequiredArgsConstructor
public class RecruitService {

	private final RecruitDAO recruitDAO;

	public JobPostVO viewRecruit(long jobPostId) {
		return recruitDAO.viewRecruit(jobPostId);
	}

	public EmployerProfilesVO viewEmployerProfile(long employerUserId) {
		return recruitDAO.viewEmployerProfile(employerUserId);
	}

	public void increaseViewCount(long jobPostId) {
		recruitDAO.increaseViewCount(jobPostId);
	}
}
