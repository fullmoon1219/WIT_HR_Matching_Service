package org.wit.hrmatching.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.ResumeMapper;
import org.wit.hrmatching.vo.ResumeVO;

@Repository
public class ResumeDAO {

	@Autowired
	private ResumeMapper resumeMapper;

	public int registerResume(ResumeVO resumeVO) {

		int flag = 1;
		int result = resumeMapper.registerResume(resumeVO);

		if (result == 1) {
			flag = 0;
		}

		return flag;
	}
}
