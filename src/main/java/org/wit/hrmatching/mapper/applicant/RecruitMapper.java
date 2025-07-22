package org.wit.hrmatching.mapper.applicant;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.wit.hrmatching.vo.application.ApplicationsVO;
import org.wit.hrmatching.vo.user.EmployerProfilesVO;
import org.wit.hrmatching.vo.job.JobPostVO;
import org.wit.hrmatching.vo.job.RecruitListVO;
import org.wit.hrmatching.vo.applicantPaging.SearchCriteria;

import java.util.List;

@Mapper
public interface RecruitMapper {

	JobPostVO selectJobPost(@Param("jobPostId") long jobPostId, @Param("isAdmin") boolean isAdmin);
	EmployerProfilesVO selectEmployerProfile(long employerUserId);

	void increaseViewCount(long jobPostId);

	int selectExistApplicationCount(@Param("userId") long userId, @Param("jobPostId") long jobPostId);
	int insertApplication(ApplicationsVO application);

	List<RecruitListVO> selectRecruitList(SearchCriteria criteria);
	int selectRecruitListCount(SearchCriteria criteria);
}
