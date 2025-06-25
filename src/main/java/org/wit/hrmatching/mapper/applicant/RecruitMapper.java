package org.wit.hrmatching.mapper.applicant;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;

@Mapper
public interface RecruitMapper {

	JobPostVO selectJobPost(long jobPostId);
	EmployerProfilesVO selectEmployerProfile(long employerUserId);

	void increaseViewCount(long jobPostId);
}
