package org.wit.hrmatching.mapper.applicant;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.ApplicationDetailVO;

import java.util.List;

@Mapper
public interface ApplicationMapper {

	List<ApplicationDetailVO> selectApplicationList(long userId);

	ApplicationDetailVO selectApplication(long applicationId);
}
