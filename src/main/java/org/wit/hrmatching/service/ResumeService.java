package org.wit.hrmatching.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wit.hrmatching.dao.ResumeDAO;
import org.wit.hrmatching.vo.ResumeVO;

@Service
public class ResumeService {

	@Autowired
	private ResumeDAO resumeDAO;

	public int registerResume(ResumeVO resumeVO) {
		return resumeDAO.registerResume(resumeVO);
	}
}
