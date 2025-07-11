package org.wit.hrmatching.mapper.applicant;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.ApplicationsVO;
import org.wit.hrmatching.vo.EmployerProfilesVO;
import org.wit.hrmatching.vo.JobPostVO;
import org.wit.hrmatching.vo.RecruitListVO;
import org.wit.hrmatching.vo.applicantPaging.SearchCriteria;

import java.util.List;

@Mapper
public interface RecruitMapper {

	JobPostVO selectJobPost(long jobPostId);
	EmployerProfilesVO selectEmployerProfile(long employerUserId);

	void increaseViewCount(long jobPostId);

	int selectExistApplicationCount(long userId, long jobPostId);
	int insertApplication(ApplicationsVO application);

	List<RecruitListVO> selectRecruitList(SearchCriteria criteria);
	int selectRecruitListCount(SearchCriteria criteria);
}
