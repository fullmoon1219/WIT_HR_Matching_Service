package org.wit.hrmatching.dao.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.applicant.ResumeMapper;
import org.wit.hrmatching.vo.ApplicantProfilesVO;
import org.wit.hrmatching.vo.ResumeVO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ResumeDAO {

	private final ResumeMapper resumeMapper;

	public int registerResume(ResumeVO resumeVO) {

		// 등록 성공 시 flag = 0, 실패 시 flag = 1
		int flag = 1;
		int result = resumeMapper.insertResume(resumeVO);

		if (result == 1) {
			flag = 0;
		}

		return flag;
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

	public int updatePrivateResume(long resumeId) {

		int flag = 1;
		int result = resumeMapper.updatePrivateResume(resumeId);

		if (result == 1) {
			flag = 0;
		}

		return flag;
	}

	public int updatePublicResume(long resumeId) {

		int flag = 1;
		int result = resumeMapper.updatePublicResume(resumeId);

		if (result == 1) {
			flag = 0;
		}

		return flag;
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

	public int editResume(ResumeVO resumeVO) {

		// 수정 성공 시 flag = 0, 실패 시 flag = 1
		int flag = 1;
		int result = resumeMapper.updateResume(resumeVO);

		if (result == 1) {
			flag = 0;
		}

		return flag;
	}

	public int deleteResume(long resumeId) {

		// 수정 성공 시 flag = 0, 실패 시 flag = 1
		int flag = 1;
		int result = resumeMapper.deleteResume(resumeId);

		if (result == 1) {
			flag = 0;
		}

		return flag;
	}

	public void updatePrimaryResume(long resumeId, long userId) {
		resumeMapper.updatePrimaryResume(resumeId, userId);
	}

	public void clearPrimaryResume(long userId) {
		resumeMapper.clearPrimaryResume(userId);
	}
}
