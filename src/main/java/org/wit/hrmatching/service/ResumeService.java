package org.wit.hrmatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.ResumeDAO;
import org.wit.hrmatching.vo.ResumeVO;

@Service
@RequiredArgsConstructor
public class ResumeService {

	private final ResumeDAO resumeDAO;

	public int registerResume(ResumeVO resumeVO) {
		return resumeDAO.registerResume(resumeVO);
	}
}
