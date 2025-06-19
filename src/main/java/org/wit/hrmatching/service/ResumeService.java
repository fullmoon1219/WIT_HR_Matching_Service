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

	public List<ResumeVO> selectResumeList(long userId) {

		return resumeDAO.selectResumeList(userId);
	}
}
