package org.wit.hrmatching.dto.applicant;

import lombok.Getter;
import lombok.Setter;
import org.wit.hrmatching.vo.application.ApplicationDetailVO;
import org.wit.hrmatching.vo.application.ApplicationsVO;

import java.util.List;

@Getter
@Setter
public class DashboardDTO {

	private ApplicantProfileDTO userProfile;

	private int applicationCount;
	private int resumeCount;
	private int bookmarkCount;

	private List<ApplicationDetailVO> recentApplicationList;
}
