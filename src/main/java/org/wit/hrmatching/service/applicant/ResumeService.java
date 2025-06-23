package org.wit.hrmatching.service.applicant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.applicant.ResumeDAO;
import org.wit.hrmatching.vo.ResumeVO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

	private final ResumeDAO resumeDAO;

	public boolean registerResume(ResumeVO resumeVO) {

		return resumeDAO.registerResume(resumeVO) == 0;
	}

	public List<ResumeVO> getResumeList(long userId) {

		return resumeDAO.getResumeList(userId);
	}

	public List<ResumeVO> getDraftResumeList(long userId) {
		return resumeDAO.getDraftResumeList(userId);
	}

	public ResumeVO getResume(long resumeId) {
		return resumeDAO.getResume(resumeId);
	}

	public ResumeVO getResumeForUpdate(long resumeId) {
		return resumeDAO.getResumeForUpdate(resumeId);
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
