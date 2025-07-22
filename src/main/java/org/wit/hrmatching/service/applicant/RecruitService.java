package org.wit.hrmatching.service.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wit.hrmatching.dao.applicant.RecruitDAO;
import org.wit.hrmatching.exception.DuplicateApplicationException;
import org.wit.hrmatching.vo.applicantPaging.PageResponseVO;
import org.wit.hrmatching.vo.applicantPaging.SearchCriteria;
import org.wit.hrmatching.vo.application.ApplicationsVO;
import org.wit.hrmatching.vo.job.JobPostVO;
import org.wit.hrmatching.vo.job.RecruitListVO;
import org.wit.hrmatching.vo.user.EmployerProfilesVO;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecruitService {

	private final RecruitDAO recruitDAO;

	public JobPostVO viewRecruit(long jobPostId, boolean isAdmin) {
		return recruitDAO.viewRecruit(jobPostId, isAdmin);
	}

	public EmployerProfilesVO viewEmployerProfile(long employerUserId) {
		return recruitDAO.viewEmployerProfile(employerUserId);
	}

	public void increaseViewCount(long jobPostId) {
		recruitDAO.increaseViewCount(jobPostId);
	}

	public Map<String, Object> getJobPostSummary(long jobPostId, boolean isAdmin) {

		JobPostVO jobPost = recruitDAO.viewRecruit(jobPostId, isAdmin);
		EmployerProfilesVO employer = recruitDAO.viewEmployerProfile(jobPost.getUserId());

		if (jobPost == null) {
			return null;
		}

		Map<String, Object> summary = new HashMap<>();
		summary.put("title", jobPost.getTitle());
		summary.put("location", jobPost.getLocation());
		summary.put("employmentType", jobPost.getEmploymentType());
		summary.put("deadline", jobPost.getDeadline());
		summary.put("salary", jobPost.getSalary());

		if (employer != null) {
			summary.put("companyName", employer.getCompanyName());
		} else {
			summary.put("companyName", "(기업 정보 미등록)");
		}

		return summary;
	}

	@Transactional
	public void applyApplication(long userId, long jobPostId, long resumeId) {

		// 중복 지원 시 1, 성공 시 0, db 연결 실패 시 2 반환

		if (recruitDAO.isApplicationExist(userId, jobPostId)) {
			throw new DuplicateApplicationException("이미 지원한 공고입니다.");
		}

		ApplicationsVO application = new ApplicationsVO();
		application.setUserId(userId);
		application.setJobPostId(jobPostId);
		application.setResumeId(resumeId);

		boolean result = recruitDAO.insertApplication(application);

		if (!result) {
			throw new RuntimeException("지원 처리 중 데이터베이스 오류가 발생했습니다.");
		}
	}

	public boolean isApplicationExist(long userId, long jobPostId) {
		return recruitDAO.isApplicationExist(userId, jobPostId);
	}

	public PageResponseVO<RecruitListVO> getRecruitList(SearchCriteria criteria) {

		int totalRecord = recruitDAO.countRecruitList(criteria);

		if (totalRecord < 1) {
			return new PageResponseVO<>(0, criteria, Collections.emptyList());
		}

		List<RecruitListVO> content = recruitDAO.getRecruitList(criteria);

		return new PageResponseVO<>(totalRecord, criteria, content);
	}
}
