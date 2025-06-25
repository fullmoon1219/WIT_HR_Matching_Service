package org.wit.hrmatching.dao.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.applicant.RecruitMapper;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;

@Repository
@RequiredArgsConstructor
public class RecruitDAO {

	private final RecruitMapper recruitMapper;

	public JobPostVO viewRecruit(long recruitId) {
		return recruitMapper.selectJobPost(recruitId);
	}

	public EmployerProfilesVO viewEmployerProfile(long employerUserId) {
		return recruitMapper.selectEmployerProfile(employerUserId);
	}
}
