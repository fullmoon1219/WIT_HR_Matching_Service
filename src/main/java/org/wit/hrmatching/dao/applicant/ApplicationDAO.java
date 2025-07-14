package org.wit.hrmatching.dao.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.applicant.ApplicationMapper;
import org.wit.hrmatching.vo.application.ApplicationDetailVO;
import org.wit.hrmatching.vo.applicantPaging.SearchCriteria;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApplicationDAO {

	private final ApplicationMapper applicationMapper;

	public List<ApplicationDetailVO> getApplicationList(long userId, SearchCriteria criteria) {
		return applicationMapper.selectApplicationList(userId, criteria);
	}

	public int countApplicationList(long userId, SearchCriteria criteria) {
		return applicationMapper.countApplicationList(userId, criteria);
	}

	public int selectCountByStatusList(long userId, List<String> statusList) {
		return applicationMapper.selectCountByStatusList(userId, statusList);
	}

	public ApplicationDetailVO getApplication(long applicationId) {
		return applicationMapper.selectApplication(applicationId);
	}
}
