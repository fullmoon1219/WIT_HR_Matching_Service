package org.wit.hrmatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.ResumeDAO;
import org.wit.hrmatching.vo.ResumeVO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

	private final ResumeDAO resumeDAO;

	public int registerResume(ResumeVO resumeVO) {
		return resumeDAO.registerResume(resumeVO);
	}

	public List<ResumeVO> getResumeList(long userId) {

		return resumeDAO.getResumeList(userId);
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

	public int editResume(ResumeVO resumeVO) {
		return resumeDAO.editResume(resumeVO);
	}
}
