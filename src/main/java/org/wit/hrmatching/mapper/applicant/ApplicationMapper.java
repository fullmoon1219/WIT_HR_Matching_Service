package org.wit.hrmatching.mapper.applicant;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.wit.hrmatching.vo.ApplicationDetailVO;
import org.wit.hrmatching.vo.applicantPaging.SearchCriteria;

import java.util.List;

@Mapper
public interface ApplicationMapper {

	List<ApplicationDetailVO> selectApplicationList(@Param("userId") long userId, @Param("criteria") SearchCriteria criteria);
	int countApplicationList(@Param("userId") long userId, @Param("criteria") SearchCriteria criteria);

	int selectCountByStatusList(@Param("userId") long userId, @Param("statusList") List<String> statusList);

	ApplicationDetailVO selectApplication(long applicationId);
}
