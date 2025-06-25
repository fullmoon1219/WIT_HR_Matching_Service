package org.wit.hrmatching.service.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.applicant.RecruitDAO;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;

import java.util.HashMap;
import java.util.Map;

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

	public Map<String, Object> getJobPostSummary(long jobPostId) {
		JobPostVO jobPost = recruitDAO.viewRecruit(jobPostId);
		EmployerProfilesVO employer = recruitDAO.viewEmployerProfile(jobPost.getUserId());

		Map<String, Object> summary = new HashMap<>();
		summary.put("title", jobPost.getTitle());
		summary.put("location", jobPost.getLocation());
		summary.put("employmentType", jobPost.getEmploymentType());
		summary.put("deadline", jobPost.getDeadline());
		summary.put("companyName", jobPost.getCompanyName());

		return summary;
	}
}
