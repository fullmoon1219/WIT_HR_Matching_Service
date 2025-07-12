package org.wit.hrmatching.service.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.applicant.ResumeDAO;
import org.wit.hrmatching.vo.ApplicantProfilesVO;
import org.wit.hrmatching.vo.ResumeVO;
import org.wit.hrmatching.vo.UserVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResumeService {

	private final ResumeDAO resumeDAO;

	public Map<String, Object> getUserProfile(long userId) {

		UserVO userInfo = resumeDAO.getUserInfo(userId);
		ApplicantProfilesVO profile = resumeDAO.getApplicantProfile(userId);

		Map<String, Object> userProfile = new HashMap<>();
		userProfile.put("userInfo", userInfo);
		userProfile.put("profile", profile);

		return userProfile;
	}

	public boolean registerResume(ResumeVO resumeVO) {
		return resumeDAO.registerResume(resumeVO);
	}

	public List<ResumeVO> getCompletedResumeList(Long userId) {

		List<ResumeVO> completedList = new ArrayList<>();

		ResumeVO publicResume = resumeDAO.getPublicResume(userId);

		if (publicResume != null) {
			completedList.add(publicResume);
		}

		List<ResumeVO> otherResumes = resumeDAO.getResumeList(userId);

		for (ResumeVO resume : otherResumes) {
			completedList.add(resume);
		}

		return completedList;
	}


	public List<ResumeVO> getDraftResumeList(long userId) {
		return resumeDAO.getDraftResumeList(userId);
	}

	public boolean setResumeAsPrivate(long resumeId, long userId) {
		resumeDAO.clearPrimaryResume(userId);
		return resumeDAO.updatePrivateResume(resumeId);
	}

	public boolean setResumeAsPublic(long resumeId, long userId) {

		resumeDAO.resetPublicResume(userId);
		resumeDAO.updatePrimaryResume(resumeId, userId);

		return resumeDAO.updatePublicResume(resumeId);
	}

	public boolean confirmProfile(long userId) {

		ApplicantProfilesVO profile = resumeDAO.getApplicantProfile(userId);

		if (profile == null) return false;

		if (profile.getAddress() == null || profile.getAddress().trim().isEmpty()) return false;
		if (profile.getPhoneNumber() == null || profile.getPhoneNumber().trim().isEmpty()) return false;
		if (profile.getPortfolioUrl() == null || profile.getPortfolioUrl().trim().isEmpty()) return false;
		if (profile.getSelfIntro() == null || profile.getSelfIntro().trim().isEmpty()) return false;

		if (profile.getAge() == null || profile.getAge() <= 0) return false;

		if (profile.getExperienceYears() == null) return false;

		if (profile.getGender() == null) return false;
		if (profile.getJobType() == null) return false;

		return true;
	}

	public ResumeVO getResume(long resumeId) {
		return resumeDAO.getResume(resumeId);
	}

	public Long findOwnerIdByResumeId(long resumeId) {
		return resumeDAO.findOwnerIdByResumeId(resumeId);
	}

	public String editResume(ResumeVO resumeVO) {

		ResumeVO existingResume = resumeDAO.getResume(resumeVO.getId());

		if (existingResume == null) {
			return "NOT_FOUND";
		}

		boolean updateSuccess = resumeDAO.editResume(resumeVO);

		if (updateSuccess) {
			return "SUCCESS";
		} else {
			return "UPDATE_FAILED";
		}
	}

	public boolean deleteResume(long resumeId) {
		return resumeDAO.deleteResume(resumeId);
	}
}
