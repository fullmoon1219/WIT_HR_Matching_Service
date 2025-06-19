package org.wit.hrmatching.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.wit.hrmatching.vo.ResumeVO;

@Mapper
public interface ResumeMapper {

	int registerResume(ResumeVO resumeVO);
}
