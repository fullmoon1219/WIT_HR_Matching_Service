package org.wit.hrmatching.service.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.applicant.RecruitDAO;
import org.wit.hrmatching.vo.ApplicationsVO;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;

import java.util.HashMap;
import java.util.List;
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
		summary.put("companyName", employer.getCompanyName());

		return summary;
	}

	public int applyApplication(long userId, long jobPostId, long resumeId) {

		// 중복 지원 시 1, 성공 시 0, db 연결 실패 시 2 반환

		if (recruitDAO.isApplicationExist(userId, jobPostId)) {
			return 1;
		}

		ApplicationsVO application = new ApplicationsVO();
		application.setUserId(userId);
		application.setJobPostId(jobPostId);
		application.setResumeId(resumeId);

		boolean result = recruitDAO.insertApplication(application);

		if (result) {
			return 0;
		} else {
			return 2;
		}
	}

	public boolean isApplicationExist(long userId, long jobPostId) {
		return recruitDAO.isApplicationExist(userId, jobPostId);
	}
}
