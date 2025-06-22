package org.wit.hrmatching.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wit.hrmatching.mapper.ResumeMapper;
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

	public List<ResumeVO> getResumeList(long userId) {
		return resumeMapper.selectResumeList(userId);
	}

	public List<ResumeVO> getDraftResumeList(long userId) {
		return resumeMapper.selectDraftResumeList(userId);
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
}
