package org.wit.hrmatching.mapper.applicant;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.wit.hrmatching.vo.application.ApplicationDetailVO;
import org.wit.hrmatching.vo.applicantPaging.SearchCriteria;

import java.util.List;

@Mapper
public interface ApplicationMapper {

	List<ApplicationDetailVO> selectApplicationList(@Param("userId") long userId, @Param("criteria") SearchCriteria criteria);
	int countApplicationList(@Param("userId") long userId, @Param("criteria") SearchCriteria criteria);

	int selectCountByStatusList(@Param("userId") long userId, @Param("statusList") List<String> statusList);

	ApplicationDetailVO selectApplication(long applicationId);

	@Select("select user_id FROM applications where id = #{applicationId}")
	Long findUserIdByApplicationId(long applicationId);

	List<ApplicationDetailVO> selectRecentApplicationsForDashboard(@Param("userId") long userId);
}
