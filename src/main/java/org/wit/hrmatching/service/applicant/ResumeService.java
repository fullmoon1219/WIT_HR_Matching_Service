package org.wit.hrmatching.service.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.applicant.ResumeDAO;
import org.wit.hrmatching.vo.ApplicantProfilesVO;
import org.wit.hrmatching.vo.ResumeVO;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

	private final ResumeDAO resumeDAO;

	public boolean registerResume(ResumeVO resumeVO) {
		return resumeDAO.registerResume(resumeVO) == 0;
	}

	public ResumeVO getPublicResume(long userId) {
		return resumeDAO.getPublicResume(userId);
	}

	public List<ResumeVO> getResumeList(long userId) {
		return resumeDAO.getResumeList(userId);
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
		return resumeDAO.updatePrivateResume(resumeId) == 0;
	}

	public boolean setResumeAsPublic(long resumeId, long userId) {

		resumeDAO.resetPublicResume(userId);
		resumeDAO.updatePrimaryResume(resumeId, userId);

		return resumeDAO.updatePublicResume(resumeId) == 0;
	}

	public boolean confirmProfile(long userId) {

		ApplicantProfilesVO profile = resumeDAO.getApplicantProfile(userId);

		if (profile == null) return false;

		return profile.getAge() != null &&
				profile.getPhoneNumber() != null &&
				profile.getAddress() != null &&
				profile.getGender() != null &&
				profile.getPortfolioUrl() != null &&
				profile.getSelfIntro() != null &&
				profile.getJobType() != null &&
				profile.getExperienceYears() != null;
	}

	public ResumeVO getResume(long resumeId) {
		return resumeDAO.getResume(resumeId);
	}

	public Long findOwnerIdByResumeId(long resumeId) {
		return resumeDAO.findOwnerIdByResumeId(resumeId);
	}

	public boolean editResume(ResumeVO resumeVO) {
		return resumeDAO.editResume(resumeVO) == 0;
	}

	public boolean deleteResume(long resumeId) {
		return resumeDAO.deleteResume(resumeId) == 0;
	}
}
