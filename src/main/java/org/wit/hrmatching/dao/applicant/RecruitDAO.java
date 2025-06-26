package org.wit.hrmatching.dao.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.applicant.RecruitMapper;
import org.wit.hrmatching.vo.ApplicationsVO;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecruitDAO {

	private final RecruitMapper recruitMapper;

	public JobPostVO viewRecruit(long jobPostId) {
		return recruitMapper.selectJobPost(jobPostId);
	}

	public EmployerProfilesVO viewEmployerProfile(long employerUserId) {
		return recruitMapper.selectEmployerProfile(employerUserId);
	}

	public void increaseViewCount(long jobPostId) {
		recruitMapper.increaseViewCount(jobPostId);
	}

	public boolean isApplicationExist(long userId, long jobPostId) {
		return recruitMapper.selectExistApplicationCount(userId, jobPostId) > 0;
	}

	public boolean insertApplication(ApplicationsVO application) {

		int rowsAffected = recruitMapper.insertApplication(application);

		return rowsAffected == 1;
	}
}
