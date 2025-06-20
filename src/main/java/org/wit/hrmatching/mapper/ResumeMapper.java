package org.wit.hrmatching.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.ResumeVO;

import java.util.List;

@Mapper
public interface ResumeMapper {

	int registerResume(ResumeVO resumeVO);

	List<ResumeVO> selectResumeList(long userId);

	ResumeVO viewResume(long resumeId);
}
