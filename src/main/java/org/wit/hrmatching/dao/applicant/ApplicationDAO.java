package org.wit.hrmatching.dao.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.applicant.ApplicationMapper;
import org.wit.hrmatching.vo.ApplicationDetailVO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApplicationDAO {

	private final ApplicationMapper applicationMapper;

	public List<ApplicationDetailVO> getApplicationList(long userId) {
		return applicationMapper.selectApplicationList(userId);
	}

	public ApplicationDetailVO getApplication(long applicationId) {
		return applicationMapper.selectApplication(applicationId);
	}
}
