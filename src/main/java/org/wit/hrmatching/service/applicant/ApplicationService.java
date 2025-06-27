package org.wit.hrmatching.service.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.applicant.ApplicationDAO;
import org.wit.hrmatching.vo.ApplicationDetailVO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

	private final ApplicationDAO applicationDAO;

	public List<ApplicationDetailVO> getApplicationList(long userId) {
		return applicationDAO.getApplicationList(userId);
	}

	public ApplicationDetailVO getApplication(long applicationId) {
		return applicationDAO.getApplication(applicationId);
	}
}
