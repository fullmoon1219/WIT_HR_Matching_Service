package org.wit.hrmatching.service.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.applicant.ApplicationDAO;
import org.wit.hrmatching.vo.application.ApplicationDetailVO;
import org.wit.hrmatching.vo.applicantPaging.PageResponseVO;
import org.wit.hrmatching.vo.applicantPaging.SearchCriteria;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

	private final ApplicationDAO applicationDAO;

	public PageResponseVO<ApplicationDetailVO> getApplicationList(long userId, SearchCriteria criteria) {

		int totalRecord = applicationDAO.countApplicationList(userId, criteria);
		int countInProgress = applicationDAO.selectCountByStatusList(userId, Arrays.asList("APPLIED"));
		int countFinal = applicationDAO.selectCountByStatusList(userId, Arrays.asList("ACCEPTED", "REJECTED"));

		if (totalRecord < 1) {
			return new PageResponseVO<>(0, criteria, Collections.emptyList(), countInProgress, countFinal);
		}

		List<ApplicationDetailVO> content = applicationDAO.getApplicationList(userId, criteria);

		return new PageResponseVO<>(totalRecord, criteria, content, countInProgress, countFinal);
	}

	public ApplicationDetailVO getApplication(long applicationId) {
		return applicationDAO.getApplication(applicationId);
	}

	public Long findOwnerIdByApplicationId(long applicationId) {
		return applicationDAO.findOwnerIdByApplicationId(applicationId);
	}
}
