package org.wit.hrmatching.dao.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.applicant.ResumeMapper;
import org.wit.hrmatching.vo.ApplicantProfilesVO;
import org.wit.hrmatching.vo.ResumeVO;
import org.wit.hrmatching.vo.UserVO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ResumeDAO {

	private final ResumeMapper resumeMapper;

	public UserVO getUserInfo(long userId) {
		return resumeMapper.selectUserInfo(userId);
	}

	public boolean registerResume(ResumeVO resumeVO) {

		int rowsAffected = resumeMapper.insertResume(resumeVO);

		return rowsAffected == 1;
	}

	public ResumeVO getPublicResume(long userId) {
		return resumeMapper.selectPublicResume(userId);
	}

	public List<ResumeVO> getResumeList(long userId) {
		return resumeMapper.selectResumeList(userId);
	}

	public List<ResumeVO> getDraftResumeList(long userId) {
		return resumeMapper.selectDraftResumeList(userId);
	}

	public boolean updatePrivateResume(long resumeId) {

		int rowsAffected = resumeMapper.updatePrivateResume(resumeId);

		return rowsAffected == 1;
	}

	public boolean updatePublicResume(long resumeId) {

		int rowsAffected = resumeMapper.updatePublicResume(resumeId);

		return rowsAffected == 1;
	}

	public void resetPublicResume(long resumeId) {
		resumeMapper.resetPublicResume(resumeId);
	}

	public ApplicantProfilesVO getApplicantProfile(long userId) {
		return resumeMapper.selectApplicantProfile(userId);
	}

	public ResumeVO getResume(long resumeId) {
		return resumeMapper.selectResume(resumeId);
	}

	public ResumeVO getResumeForUpdate(long resumeId) {
		return resumeMapper.selectResumeForUpdate(resumeId);
	}

	public Long findOwnerIdByResumeId(long resumeId) {
		return resumeMapper.findOwnerIdByResumeId(resumeId);
	}

	public boolean editResume(ResumeVO resumeVO) {

		int rowsAffected = resumeMapper.updateResume(resumeVO);

		return rowsAffected == 1;
	}

	public boolean deleteResume(long resumeId) {

		int rowsAffected = resumeMapper.deleteResume(resumeId);

		return rowsAffected == 1;
	}

	public void updatePrimaryResume(long resumeId, long userId) {
		resumeMapper.updatePrimaryResume(resumeId, userId);
	}

	public void clearPrimaryResume(long userId) {
		resumeMapper.clearPrimaryResume(userId);
	}
}
