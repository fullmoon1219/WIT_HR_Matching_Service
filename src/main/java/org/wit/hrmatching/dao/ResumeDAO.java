package org.wit.hrmatching.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.ResumeMapper;
import org.wit.hrmatching.vo.ResumeVO;

@Repository
@RequiredArgsConstructor
public class ResumeDAO {

	private final ResumeMapper resumeMapper;

	public int registerResume(ResumeVO resumeVO) {

		int flag = 1;
		int result = resumeMapper.registerResume(resumeVO);

		if (result == 1) {
			flag = 0;
		}

		return flag;
	}
}
